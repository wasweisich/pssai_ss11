package ttp.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TTPSolutionTest {

    @Test
    public void compare_shouldRankLowerCostFirst() {
        TTPSolution s1 = new TTPSolution();
        s1.setCost(1);

        TTPSolution s2 = new TTPSolution();
        s2.setCost(2);

        List<TTPSolution> solutions = new ArrayList<TTPSolution>(2);
        solutions.add(s2);
        solutions.add(s1);

        assertThat(solutions.get(0), is(s2));

        Collections.sort(solutions);

        assertThat(solutions.get(0), is(s1));
    }

}
