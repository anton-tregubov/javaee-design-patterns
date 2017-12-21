package ru.faulab.javaee.design.patterns.sample.project.platform.impl.openapi;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static ru.faulab.javaee.design.patterns.sample.project.platform.impl.SampleProjectApplication.GLOBAL_REST_ADI_PREFIX;

@WebServlet(name = "OpenApiServlet", urlPatterns = "/open-api")
public class OpenApiServlet extends HttpServlet {
    private static final String WEBJARS_SWAGGER_UI = "./webjars/swagger-ui/3.6.1/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            AtomicReference<Supplier<String>> contentSupplier = new AtomicReference<>();
            AtomicReference<ContentConsumer> contentConsumer = new AtomicReference<>();
            request.getRequestDispatcher(WEBJARS_SWAGGER_UI + "index.html").include(request, new HttpServletResponseWrapper(response) {
                @Override
                public ServletOutputStream getOutputStream() throws IOException {
                    ByteArrayOutputStream originalResourceOutputStream = Optional.ofNullable(getHeader(HttpHeaders.CONTENT_LENGTH)).map(Integer::parseInt).map(ByteArrayOutputStream::new).orElseGet(ByteArrayOutputStream::new);
                    ServletOutputStream originalStream = super.getOutputStream();
                    contentSupplier.set(() -> new String(originalResourceOutputStream.toByteArray(), StandardCharsets.UTF_8));
                    contentConsumer.set(originalStream::print);
                    return new ServletOutputStream() {
                        @Override
                        public boolean isReady() {
                            return originalStream.isReady();
                        }

                        @Override
                        public void setWriteListener(WriteListener writeListener) {
                            originalStream.setWriteListener(writeListener);
                        }

                        @Override
                        public void write(int b) {
                            originalResourceOutputStream.write(b);
                        }
                    };
                }

                @Override
                public PrintWriter getWriter() {
                    StringWriter originalResourceWriter = new StringWriter();
                    contentSupplier.set(originalResourceWriter::toString);
                    contentConsumer.set(content -> getWriter().print(content));
                    return new PrintWriter(originalResourceWriter);
                }
            });
            String content = contentSupplier.get().get();
            contentConsumer.get().accept(updateSwaggerUi(content));
        } catch (IOException | ServletException e) {
            getServletContext().log("Unable to load resource", e);
        }
    }

    private String updateSwaggerUi(String content) {
        return content
                .replaceAll(Pattern.quote("\"./"), "\"" + WEBJARS_SWAGGER_UI)
                .replaceAll("url:\\s*\"[\\p{Graph}&&[^\"]]+\"", "url: \"" + GLOBAL_REST_ADI_PREFIX + "/openapi.yaml\"")
                .replaceAll("SwaggerUIStandalonePreset", "SwaggerUIStandalonePreset.slice(1)");
    }

    interface ContentConsumer {
        void accept(String s) throws IOException;
    }
}
