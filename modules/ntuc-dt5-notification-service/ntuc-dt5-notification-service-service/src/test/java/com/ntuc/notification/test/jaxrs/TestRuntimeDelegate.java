package com.ntuc.notification.test.jaxrs;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.RuntimeDelegate;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.*;

/**
 * Test-only RuntimeDelegate so JAX-RS Response works
 * in plain JUnit (no Jersey / RESTEasy).
 *
 * Compatible with Liferay DXP 7.3 (JAX-RS 2.x).
 */
public class TestRuntimeDelegate extends RuntimeDelegate {

    @Override
    public Response.ResponseBuilder createResponseBuilder() {
        return new SimpleResponseBuilder();
    }

    @Override
    public UriBuilder createUriBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Link.Builder createLinkBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Variant.VariantListBuilder createVariantListBuilder() {
        throw new UnsupportedOperationException();
    }

    /** Install once before tests */
    public static void install() {
        RuntimeDelegate.setInstance(new TestRuntimeDelegate());
    }

    // ---------------------------------------------------------------------

    private static class SimpleResponseBuilder extends Response.ResponseBuilder {

        private int status = 200;
        private Object entity;
        private final MultivaluedMap<String, Object> headers =
            new MultivaluedHashMap<>();

        @Override
        public Response build() {
            return new SimpleResponse(status, entity, headers);
        }

        @Override
        public Response.ResponseBuilder clone() {
            SimpleResponseBuilder b = new SimpleResponseBuilder();
            b.status = status;
            b.entity = entity;
            b.headers.putAll(headers);
            return b;
        }

        @Override
        public Response.ResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        @Override
        public Response.ResponseBuilder status(int status, String reasonPhrase) {
            this.status = status;
            return this;
        }

        @Override
        public Response.ResponseBuilder entity(Object entity) {
            this.entity = entity;
            return this;
        }

        @Override
        public Response.ResponseBuilder entity(Object entity, Annotation[] annotations) {
            this.entity = entity;
            return this;
        }

        @Override
        public Response.ResponseBuilder header(String name, Object value) {
            headers.add(name, value);
            return this;
        }

        // ---- Required abstract methods (no-op impls) ----

        @Override public Response.ResponseBuilder allow(String... methods) { return this; }
        @Override public Response.ResponseBuilder allow(Set<String> methods) { return this; }
        @Override public Response.ResponseBuilder cacheControl(CacheControl cc) { return this; }
        @Override public Response.ResponseBuilder encoding(String encoding) { return this; }
        @Override public Response.ResponseBuilder expires(Date date) { return this; }
        @Override public Response.ResponseBuilder language(String lang) { return this; }
        @Override public Response.ResponseBuilder language(Locale lang) { return this; }
        @Override public Response.ResponseBuilder lastModified(Date date) { return this; }
        @Override public Response.ResponseBuilder location(URI uri) { return this; }
        @Override public Response.ResponseBuilder tag(EntityTag tag) { return this; }
        @Override public Response.ResponseBuilder tag(String tag) { return this; }
        @Override public Response.ResponseBuilder type(MediaType type) { return this; }
        @Override public Response.ResponseBuilder type(String type) { return this; }
        @Override public Response.ResponseBuilder variant(Variant v) { return this; }

        @Override
        public Response.ResponseBuilder variants(Variant... variants) {
            return this;
        }

        @Override
        public Response.ResponseBuilder variants(List<Variant> variants) {
            return this;
        }

        @Override public Response.ResponseBuilder contentLocation(URI uri) { return this; }
        @Override public Response.ResponseBuilder cookie(NewCookie... cookies) { return this; }
        @Override public Response.ResponseBuilder link(URI uri, String rel) { return this; }
        @Override public Response.ResponseBuilder link(String uri, String rel) { return this; }
        @Override public Response.ResponseBuilder links(Link... links) { return this; }

        @Override
        public Response.ResponseBuilder replaceAll(
            MultivaluedMap<String, Object> headers) {

            this.headers.clear();
            this.headers.putAll(headers);
            return this;
        }
    }

    // ---------------------------------------------------------------------

    private static class SimpleResponse extends Response {

        private final int status;
        private final Object entity;
        private final MultivaluedMap<String, Object> headers;

        SimpleResponse(
            int status, Object entity, MultivaluedMap<String, Object> headers) {

            this.status = status;
            this.entity = entity;
            this.headers = headers;
        }

        @Override public int getStatus() { return status; }
        @Override public StatusType getStatusInfo() { return Status.fromStatusCode(status); }
        @Override public Object getEntity() { return entity; }
        @Override public boolean hasEntity() { return entity != null; }

        @Override
        public MultivaluedMap<String, Object> getMetadata() {
            return headers;
        }

        @Override
        public MultivaluedMap<String, String> getStringHeaders() {
            MultivaluedMap<String, String> out = new MultivaluedHashMap<>();
            headers.forEach((k, v) ->
                v.forEach(o -> out.add(k, String.valueOf(o))));
            return out;
        }

        @Override public String getHeaderString(String name) {
            Object v = headers.getFirst(name);
            return v == null ? null : String.valueOf(v);
        }

        // ---- Not required in unit tests ----
        @Override public <T> T readEntity(Class<T> type) { throw new UnsupportedOperationException(); }
        @Override public <T> T readEntity(GenericType<T> type) { throw new UnsupportedOperationException(); }
        @Override public <T> T readEntity(Class<T> type, Annotation[] ann) { throw new UnsupportedOperationException(); }
        @Override public <T> T readEntity(GenericType<T> type, Annotation[] ann) { throw new UnsupportedOperationException(); }
        @Override public boolean bufferEntity() { return false; }
        @Override public void close() {}
        @Override public MediaType getMediaType() { return null; }
        @Override public Locale getLanguage() { return null; }
        @Override public int getLength() { return -1; }
        @Override public Set<String> getAllowedMethods() { return Collections.emptySet(); }
        @Override public Map<String, NewCookie> getCookies() { return Collections.emptyMap(); }
        @Override public EntityTag getEntityTag() { return null; }
        @Override public Date getDate() { return null; }
        @Override public Date getLastModified() { return null; }
        @Override public URI getLocation() { return null; }
        @Override public Set<Link> getLinks() { return Collections.emptySet(); }
        @Override public boolean hasLink(String relation) { return false; }
        @Override public Link getLink(String relation) { return null; }
        @Override public Link.Builder getLinkBuilder(String relation) { return null; }
    }
}
