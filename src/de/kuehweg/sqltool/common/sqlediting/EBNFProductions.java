/*
 * Copyright (c) 2013, Michael Kühweg
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

/**
 * Syntaxbeschreibungen
 *
 * @author Michael Kühweg
 */
public class EBNFProductions {

    public static final String SUBQUERY =
            "subquery ::=\n"
            + "  SELECT [ LIMIT n m ] [ DISTINCT | ALL ] select_list\n"
            + "  FROM join_source\n"
            + "  [ where_clause ]\n"
            + "  [ group_by_clause ]\n"
            + "  [ compound_operator subquery ]\n"
            + "  [ order_by_clause ]\n";
    public static final String SELECT_LIST =
            "select_list ::=\n"
            + "  {*\n"
            + "  | { [schema.] { table | view } .* | expr }\n"
            + "    [ , { [schema.] { table | view } .* | expr } ]...\n"
            + "  }\n";
    public static final String COMPOUND_OPERATOR =
            "compound_operator ::=\n"
            + "  { UNION [ ALL ] | INTERSECT | EXCEPT }\n";
    public static final String JOIN_SOURCE =
            "join_source ::=\n"
            + "  { table_reference | join_clause | ( join_clause ) }\n"
            + "  [ , { table_reference | join_clause | ( join_clause ) } ]...\n";
    public static final String JOIN_CLAUSE =
            "join_clause ::=\n  table_reference { inner_cross_join_clause | outer_join_clause }...\n";
    public static final String INNER_CROSS_JOIN_CLAUSE =
            "inner_cross_join_clause ::=\n"
            + "  {\n"
            + "     [ INNER ] JOIN table_reference ON condition\n"
            + "   | CROSS JOIN table_reference\n"
            + "  }\n";
    public static final String OUTER_JOIN_CLAUSE =
            "outer_join_clause ::=\n"
            + "  { LEFT | RIGHT | FULL } [ OUTER ] JOIN table_reference ON condition\n";
    public static final String TABLE_REFERENCE =
            "table_reference ::=\n"
            + "  query_table_expression [ t_alias ]\n";
    public static final String QUERY_TABLE_EXPRESSION =
            "query_table_expression ::=\n"
            + "  { [schema .] { table | view } | ( subquery ) }\n";
    public static final String WHERE_CLAUSE =
            "where_clause::=\n"
            + "  WHERE condition\n";
    public static final String GROUP_BY_CLAUSE =
            "group_by_clause::=\n"
            + "  GROUP BY{ expr } [, { expr }]...\n"
            + "  [ HAVING condition ]\n";
    public static final String ORDER_BY_CLAUSE =
            "order_by_clause ::=\n"
            + "  ORDER BY\n"
            + "  { expr | position }\n"
            + "  [ ASC | DESC ] [ NULLS FIRST | NULLS LAST ]\n"
            + "  [ , { expr | position }\n"
            + "  [ ASC | DESC ] [ NULLS FIRST | NULLS LAST ]]...\n";
    public static final String INSERT =
            "insert ::=\n"
            + "  INSERT insert_into_clause { values_clause | subquery }\n";
    public static final String INSERT_INTO_CLAUSE =
            "insert_into_clause ::=\n"
            + "  INTO table_reference [ ( column [, column]... ) ]\n";
    public static final String VALUES_CLAUSE =
            "values_clause ::=\n"
            + "  VALUES ( expr [, expr]... ) [, ( expr [, expr]... ) ]...\n";
    public static final String UPDATE =
            "update ::=\n"
            + "  UPDATE table_reference\n"
            + "  update_set_clause\n"
            + "  [ where_clause ]\n";
    public static final String UPDATE_SET_CLAUSE = "update_set_clause ::=\n"
            + "  SET column = { expr | ( subquery ) }\n"
            + "  [ , column = { expr | ( subquery ) } ]...\n";
    public static final String DELETE =
            "delete ::=\n  DELETE FROM table_reference\n  [ where_clause ]\n";
    public static final String CREATE_TABLE = "create_table ::=\n"
            + "  CREATE [ TEMP | CACHED | MEMORY | TEXT ] TABLE [ schema . ] table_name\n"
            + "  [ ( relational_properties ) ] [ table_properties ]\n";
    public static final String RELATIONAL_PROPERTIES =
            "relational_properties ::=\n"
            + "  column datatype [ inline_constraint [ inline_constraint ]... ]\n"
            + "  [ , column datatype [ inline_constraint [ inline_constraint ]... ] ]\n";
    public static final String TABLE_PROPERTIES = "table_properties ::=\n"
            + "  [ AS subquery ] WITH [ NO ] DATA\n";
    public static final String DROP_TABLE = "drop_table ::=\n"
            + "  DROP TABLE [ schema. ] table_name [ IF EXISTS ] [ RESTRICT | CASCADE ]\n";
}
