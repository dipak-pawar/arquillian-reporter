package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.builder.AbstractSectionBuilder;
import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSectionBuilderImpl extends AbstractSectionBuilder<TestClassReport, TestClassSectionBuilderImpl> {

    public TestClassSectionBuilderImpl(TestClassReport sectionReport) {
        super(sectionReport);
    }

    public TestClassSectionBuilderImpl stop() {
        getSectionReport().setStop(Utils.getCurrentDate());
        return this;
    }
}
