"""Generate an Excel coverage-readiness report for ntuc-dt5-notification-service API classes."""
from pathlib import Path
import re
import zipfile
from xml.sax.saxutils import escape

ROOT = Path(__file__).resolve().parents[2] / "modules/ntuc-dt5-notification-service/ntuc-dt5-notification-service-api/src/main/java"
OUTPUT = Path(__file__).resolve().parents[1] / "ntuc-dt5-notification-service-api-coverage.xlsx"

HEADERS = ["File Name", "Package", "Testable", "Must Refactor Name", "Explanation"]


def get_package(text: str) -> str:
    for line in text.splitlines():
        line = line.strip()
        if line.startswith("package "):
            return line.replace("package", "").replace(";", "").strip()
    return ""


def classify(path: Path, text: str):
    lower_path = str(path).lower()
    is_interface = bool(re.search(r"\binterface\b", text))
    is_enum = bool(re.search(r"\benum\b", text))

    if "constants" in lower_path:
        return "No", "Static constants container; negligible logic, unit tests add little value."
    if "exception" in lower_path:
        return "Yes", "Custom exception type; trivial to cover via construction and message assertions."
    if is_enum:
        return "Yes", "Enum with fixed values; simple to instantiate and validate constants for full coverage."
    if "model" in lower_path or "/dto/" in lower_path:
        return "Yes", "Data holder/DTO; straightforward bean-style tests across getters/setters and equality helpers."
    if "api/dto" in lower_path:
        return "Yes", "DTO definitions; bean-style assertions reach near-100% coverage with minimal setup."
    if "util" in lower_path:
        return "Conditional", "Utility helpers mix static logic and framework calls; isolate pure methods with unit tests, mock external dependencies otherwise."
    if "wrapper" in lower_path:
        return "No", "Framework wrapper delegates to generated services; high coverage requires integration scaffolding rather than pure unit tests."
    if "persistence" in lower_path:
        return "No", "ServiceBuilder persistence interfaces; rely on container-managed implementations, so meaningful coverage needs integration tests."
    if "service" in lower_path:
        if is_interface:
            return "No", "Service interface intended for implementation elsewhere; unit coverage belongs to implementing classes, not the API contract."
        return "Conditional", "Service helper with external dependencies; achievable coverage by mocking collaborators."
    if "email" in lower_path:
        return "Conditional", "Email contract/helpers depend on mail infrastructure; cover token builders with mocks, skip transport pieces."
    if "audit" in lower_path:
        if is_interface:
            return "No", "Audit API contracts; implementations carry behavior, interfaces alone have minimal unit-test surface."
        return "Conditional", "Audit support classes combine DTOs and framework touchpoints; DTOs are testable, framework calls need mocks."
    return "Conditional", "Mixed responsibilities; review for external dependencies and mock where possible to reach target coverage."


def gather_rows():
    rows = [HEADERS]
    for file in sorted(ROOT.rglob("*.java")):
        text = file.read_text(encoding="utf-8", errors="ignore")
        package = get_package(text)
        testable, explanation = classify(file, text)
        rows.append([
            file.relative_to(ROOT.parent.parent.parent).as_posix(),
            package,
            testable,
            "-",
            explanation,
        ])
    return rows


def write_xlsx(path: Path, rows):
    path.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(path, "w", compression=zipfile.ZIP_DEFLATED) as zf:
        zf.writestr(
            "[Content_Types].xml",
            """<?xml version='1.0' encoding='UTF-8'?>
<Types xmlns='http://schemas.openxmlformats.org/package/2006/content-types'>
  <Default Extension='rels' ContentType='application/vnd.openxmlformats-package.relationships+xml'/>
  <Default Extension='xml' ContentType='application/xml'/>
  <Override PartName='/xl/workbook.xml' ContentType='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml'/>
  <Override PartName='/xl/worksheets/sheet1.xml' ContentType='application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml'/>
</Types>""",
        )
        zf.writestr(
            "_rels/.rels",
            """<?xml version='1.0' encoding='UTF-8'?>
<Relationships xmlns='http://schemas.openxmlformats.org/package/2006/relationships'>
  <Relationship Id='rId1' Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument' Target='xl/workbook.xml'/>
</Relationships>""",
        )
        zf.writestr(
            "xl/_rels/workbook.xml.rels",
            """<?xml version='1.0' encoding='UTF-8'?>
<Relationships xmlns='http://schemas.openxmlformats.org/officeDocument/2006/relationships'>
  <Relationship Id='rId1' Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet' Target='worksheets/sheet1.xml'/>
</Relationships>""",
        )
        zf.writestr(
            "xl/workbook.xml",
            """<?xml version='1.0' encoding='UTF-8'?>
<workbook xmlns='http://schemas.openxmlformats.org/spreadsheetml/2006/main' xmlns:r='http://schemas.openxmlformats.org/officeDocument/2006/relationships'>
  <sheets>
    <sheet name='Coverage Readiness' sheetId='1' r:id='rId1'/>
  </sheets>
</workbook>""",
        )
        sheet_rows = []
        for r_idx, row in enumerate(rows, start=1):
            cells = []
            for c_idx, value in enumerate(row, start=1):
                col_letter = ""
                n = c_idx
                while n:
                    n, rem = divmod(n - 1, 26)
                    col_letter = chr(65 + rem) + col_letter
                cell_ref = f"{col_letter}{r_idx}"
                cells.append(
                    f"<c r='{cell_ref}' t='inlineStr'><is><t>{escape(str(value))}</t></is></c>"
                )
            sheet_rows.append(f"<row r='{r_idx}'>{''.join(cells)}</row>")
        sheet_xml = (
            """<?xml version='1.0' encoding='UTF-8'?>
<worksheet xmlns='http://schemas.openxmlformats.org/spreadsheetml/2006/main'>
  <sheetData>"""
            + "".join(sheet_rows)
            + "</sheetData></worksheet>"
        )
        zf.writestr("xl/worksheets/sheet1.xml", sheet_xml)


def main():
    rows = gather_rows()
    write_xlsx(OUTPUT, rows)
    print(f"Wrote {OUTPUT} with {len(rows) - 1} entries")


if __name__ == "__main__":
    main()
