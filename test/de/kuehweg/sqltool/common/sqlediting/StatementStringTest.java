/*
 * Copyright (c) 2015, Michael Kühweg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.kuehweg.sqltool.common.sqlediting;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael Kühweg
 */
public class StatementStringTest {

    public StatementStringTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void empty() {
        Assert.assertTrue(new StatementString(null).isEmpty());
        Assert.assertTrue(new StatementString("").isEmpty());
        Assert.assertTrue(new StatementString("     ").isEmpty());
        Assert.assertTrue(new StatementString("\t").isEmpty());
        Assert.assertTrue(new StatementString("\n").isEmpty());

        Assert.assertTrue(new StatementString("--").isEmpty());
        Assert.assertTrue(new StatementString("/* empty */").isEmpty());

        Assert.assertTrue(new StatementString(" ; ").isEmpty());
    }

    @Test
    public void notEmpty() {
        Assert.assertFalse(new StatementString("values").isEmpty());
    }

    @Test
    public void uncomment() {
        Assert.assertEquals("select * from table;", new StatementString(
                "/* Test */ select * --\nfrom table;").uncommentedStatement());
    }

    @Test
    public void uncommentWithCommentInsideLiteral() {
        Assert.assertEquals("select '/* literally a comment */' from table;",
                new StatementString(
                        "/* Test */ select '/* literally a comment */' --\nfrom table;").
                uncommentedStatement());
    }

    @Test
    public void original() {
        final String sql = "/* Test */ select * --\nfrom table;";
        StatementString statement = new StatementString(sql);
        Assert.assertEquals("select * from table;", statement.
                uncommentedStatement());
        Assert.assertEquals(sql, statement.originalStatement());
    }

    @Test
    public void firstKeyword() {
        Assert.assertEquals("SELECT", new StatementString(
                "/* Test */ select * --\nfrom table;").firstKeyword());
        Assert.assertEquals("DROP", new StatementString(
                "/* select */ drop --\n table;").firstKeyword());
    }
    
    @Test
    public void statementType() {
        Assert.assertTrue(new StatementString("/* select */ select * from table").isDataManipulationStatement());
        Assert.assertTrue(new StatementString("/* select */ select(1+1) from table").isDataManipulationStatement());
        // auch mit falscher Syntax bezogen auf die komplette Anweisung
        Assert.assertTrue(new StatementString("/* comment */ insert into whatever").isDataManipulationStatement());

        // auch mit falscher Syntax bezogen auf die komplette Anweisung
        Assert.assertTrue(new StatementString("DROP SELECT").isDataDefinitionStatement());
        Assert.assertTrue(new StatementString("ALTER TABLE").isDataDefinitionStatement());

        Assert.assertTrue(new StatementString("GRANT select").isDataControlStatement());

        Assert.assertTrue(new StatementString("COMMIT;").isTransactionControlStatement());
    }

    @Test
    public void notMyStatementType() {
        Assert.assertFalse(new StatementString("/* select */ select * from table").isDataControlStatement());
        Assert.assertFalse(new StatementString("/* select */ select * from table").isDataDefinitionStatement());
        Assert.assertFalse(new StatementString("/* select */ select * from table").isTransactionControlStatement());

        Assert.assertFalse(new StatementString("current_date").isDataControlStatement());
        Assert.assertFalse(new StatementString("current_date").isDataDefinitionStatement());
        Assert.assertFalse(new StatementString("current_date").isDataManipulationStatement());
        Assert.assertFalse(new StatementString("current_date").isTransactionControlStatement());
    }
}
