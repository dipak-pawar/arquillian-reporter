package org.arquillian.reporter.impl.section;

import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestSuiteConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getSubsectionsOfSomeSection;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteSectionTreeEventManagerTest {

    @Test
    public void testAddingTestSuiteSectionsWithReportsUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);

        assertThat(sections).hasSize(1);
        assertThatExecutionReport(executionReport)
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class)
            .sectionTree()
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(1 + EXPECTED_NUMBER_OF_SECTIONS)
            .associatedReport();
    }

    @Test
    public void testAddingTestSuiteConfigurationSectionsWithReportsUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(
            feedWithTestSuiteConfigurationSections(executionReport,
                                                   getSubsectionsOfSomeSection(ExecutionSection.class, sections)));

        int parentCount = 1 + EXPECTED_NUMBER_OF_SECTIONS;
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        assertThat(sections).hasSize(parentCount);
        assertThatExecutionReport(executionReport)
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class, ConfigurationReport.class)
            .sectionTree()
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }
}
