package org.arquillian.reporter.impl.section;

import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestClassSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestMethodConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestMethodFailureSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestMethodSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getSubsectionsOfSomeSection;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodSectionTreeEventManagerTest {

    @Test
    public void testAddTestMethodSectionsWithReportsUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(
            feedWithTestClassSections(executionReport, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(
            feedWithTestMethodSections(executionReport, getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));

        int parentCount = (1 + EXPECTED_NUMBER_OF_SECTIONS * 2);
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        assertThat(sections).hasSize(parentCount);

        assertThatExecutionReport(executionReport)
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class,
                                                    TestMethodReport.class)
            .sectionTree()
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }

    @Test
    public void testAddTestMethodConfigurationSectionsWithReportsUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(feedWithTestClassSections(executionReport,
                                                  getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(feedWithTestMethodSections(executionReport,
                                                   getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));
        sections.putAll(feedWithTestMethodConfigurationSections(executionReport,
                                                                getSubsectionsOfSomeSection(TestClassSection.class,
                                                                                            sections)));

        int parentCount = (int) (1 + (EXPECTED_NUMBER_OF_SECTIONS * 2) + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3));
        assertThat(sections).hasSize(parentCount);

        assertThatExecutionReport(executionReport)
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class,
                                                    TestMethodReport.class, ConfigurationReport.class)
            .sectionTree()
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }

    @Test
    public void testAddTestMethodFailureSectionsWithReportsUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(feedWithTestClassSections(executionReport,
                                                  getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(feedWithTestMethodSections(executionReport,
                                                   getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));
        sections.putAll(
            feedWithTestMethodFailureSections(executionReport,
                                              getSubsectionsOfSomeSection(TestClassSection.class, sections)));

        int parentCount = (int) (1 + (EXPECTED_NUMBER_OF_SECTIONS * 2) + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3));
        assertThat(sections).hasSize(parentCount);

        assertThatExecutionReport(executionReport)
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class,
                                                    TestMethodReport.class, FailureReport.class)
            .sectionTree().wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }
}
