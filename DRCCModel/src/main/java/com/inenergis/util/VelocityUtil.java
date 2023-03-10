package com.inenergis.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Map;

public class VelocityUtil {

    public VelocityUtil() {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, org.apache.velocity.runtime.log.NullLogChute.class.getName());
        Velocity.init();
    }
    public Pair renderTemplate(String templateBodyName, String templateSubjectName, Map<String, Object> values) {
        VelocityContext context = buildVelocityContext(values);
        final String subject = buildTemplate(templateSubjectName, context);
        final String body = buildTemplate(templateBodyName, context);
        return new ImmutablePair(subject,body);
    }

    public String buildTemplate(String templateName, VelocityContext context) {
        StringWriter writerSubject = new StringWriter();
        final Template templateSubject = Velocity.getTemplate(templateName);
        templateSubject.merge(context, writerSubject);
        return writerSubject.toString();
    }

    public VelocityContext buildVelocityContext(Map<String, Object> values) {
        VelocityContext context = new VelocityContext();
        if (values != null && values.size() > 0) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }
}
