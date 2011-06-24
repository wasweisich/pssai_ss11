package ttp.main;

import ttp.metaheuristic.LocalSearchStatistics;
import ttp.metaheuristic.SearchStatistics;
import ttp.model.TTPSolution;

/**
* ${DESCRIPTION}
* <p/>
* <p><b>Company:</b>
* SAT, Research Studios Austria</p>
* <p/>
* <p><b>Copyright:</b>
* (c) 2011</p>
* <p/>
* <p><b>last modified:</b><br/>
* $Author: $<br/>
* $Date: $<br/>
* $Revision: $</p>
*
* @author patrick
*/
public class TTPResult {
    private TTPSolution ttpSolution;
    private LocalSearchStatistics localSearchStatistics;
    private SearchStatistics searchStatistics;

    public TTPResult(TTPSolution ttpSolution, LocalSearchStatistics localSearchStatistics,
                     SearchStatistics searchStatistics) {
        this.ttpSolution = ttpSolution;
        this.localSearchStatistics = localSearchStatistics;
        this.searchStatistics = searchStatistics;
    }

    public TTPSolution getTtpSolution() {
        return ttpSolution;
    }

    public LocalSearchStatistics getLocalSearchStatistics() {
        return localSearchStatistics;
    }

    public SearchStatistics getSearchStatistics() {
        return searchStatistics;
    }
}
