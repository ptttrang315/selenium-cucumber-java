package com.trangptt.library.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import com.trangptt.library.file.FileHelpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static com.trangptt.library.configuration.Constants.CURRENT_WORKING_DIR;
import static com.trangptt.library.configuration.Constants.TARGET_DIR;

public class TemplateHelpers {
    private static TemplateHelpers m_instance;
    private static String m_templatesDirectory;
    private Configuration m_freeMarkerConfig;

    public TemplateHelpers() {
        m_freeMarkerConfig = getDefaultConfiguration();
    }

    public TemplateHelpers(String templatesDirectory) throws IOException {
        m_freeMarkerConfig = getDefaultConfiguration();
        m_templatesDirectory = templatesDirectory;
        m_freeMarkerConfig.setDirectoryForTemplateLoading(new File(m_templatesDirectory));
    }

    public static TemplateHelpers getInstance() {
        if (m_instance == null)
            m_instance = new TemplateHelpers();
        return m_instance;
    }

    private static Configuration getDefaultConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setClassicCompatible(true);
        return configuration;
    }

    public static String processTemplate(String templateString, Object variables) throws Exception {
        if (variables == null) return templateString;
        Configuration templateConfiguration = getDefaultConfiguration();
        Template template = new Template("template", new StringReader(templateString), templateConfiguration);
        StringWriter stringWriter = new StringWriter();
        template.process(variables, stringWriter);
        return stringWriter.toString();
    }

    public static String processFileTemplate(String sourcePath, Object variables, String targetPath) throws IOException, TemplateException {
        sourcePath = FileHelpers.getPath(sourcePath);
        String templateName = sourcePath.substring(sourcePath.lastIndexOf(File.separator) + 1).trim();
        String templateDirectory = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator)).trim();
        Configuration templateConfiguration = getDefaultConfiguration();
        templateConfiguration.setDirectoryForTemplateLoading(new File(templateDirectory));
        if (targetPath == null) {
            File tempFile = File.createTempFile("output", "_" + templateName,
                    new File(System.getProperty(CURRENT_WORKING_DIR) + File.separator + TARGET_DIR));
            targetPath = tempFile.getPath();
        }
        Template template = templateConfiguration.getTemplate(templateName);
        Writer outputFile = null;
        try {
            outputFile = new FileWriter(targetPath);
            template.process(variables, outputFile);
            outputFile.flush();
        } catch (IOException | TemplateException ex) {
            throw ex;
        } finally {
            if (null != outputFile) {
                outputFile.close();
            }
        }
        return targetPath;
    }

}
