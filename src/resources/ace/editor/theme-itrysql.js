ace
		.define(
				"ace/theme/itrysql",
				[ "require", "exports", "module", "ace/lib/dom" ],
				function(e, t, n) {
					"use strict";
							t.isDark = !1,
							t.cssText = '.ace-itrysql .ace_gutter {background: transparent;border-right: 1px solid transparent;color:#e0e2e5;}'
									+ '.ace-itrysql .ace_print-margin {width: 1px;background: #FFFFFF;}'
									+ '.ace-itrysql {background-color: #FFFFFF;color: black;line-height:1.4375;}'
									+ '.ace-itrysql .ace_fold {background-color: rgb(60, 76, 114);}'
									+ '.ace-itrysql .ace_cursor,'
									+ '.ace-itrysql .ace_storage,'
									+ '.ace-itrysql .ace_keyword,'
									+ '.ace-itrysql .ace_variable {font-weight: bold;color: rgb(52, 101, 164);}'
									+ '.ace-itrysql .ace_constant.ace_buildin {color: rgb(88, 72, 246);}'
									+ '.ace-itrysql .ace_constant.ace_library {color: rgb(6, 150, 14);}'
									+ '.ace-itrysql .ace_function {color: rgb(60, 76, 114);}'
									+ '.ace-itrysql .ace_string {color: #b35e14;}'
									+ '.ace-itrysql .ace_comment {color: #75787b;}'
									+ '.ace-itrysql .ace_comment.ace_doc {color: #75787b;}'
									+ '.ace-itrysql .ace_comment.ace_doc.ace_tag {color: #75787b;}'
									+ '.ace-itrysql .ace_constant.ace_numeric {color: darkblue;}'
									+ '.ace-itrysql .ace_tag {color: rgb(25, 118, 116);}'
									+ '.ace-itrysql .ace_type {color: rgb(127, 0, 127);}'
									+ '.ace-itrysql .ace_xml-pe {color: rgb(104, 104, 91);}'
									+ '.ace-itrysql .ace_marker-layer .ace_selection {background: rgb(181, 213, 255);}'
									+ '.ace-itrysql .ace_marker-layer .ace_bracket {margin: -1px 0 0 -1px;border: 1px solid rgb(192, 192, 192);}'
									+ '.ace-itrysql .ace_meta.ace_tag {color:rgb(25, 118, 116);}'
									+ '.ace-itrysql .ace_invisible {color: #ddd;}'
									+ '.ace-itrysql .ace_entity.ace_other.ace_attribute-name {color:rgb(127, 0, 127);}'
									+ '.ace-itrysql .ace_marker-layer .ace_step {background: rgb(255, 255, 0);}'
									+ '.ace-itrysql .ace_active-line {background: #e8f2ff;}'
									+ '.ace-itrysql .ace_gutter-active-line {background-color : transparent;}'
									+ '.ace-itrysql .ace_marker-layer .ace_selected-word {border: 1px solid rgb(181, 213, 255);}'
									+ '.ace-itrysql .ace_indent-guide {background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==") right repeat-y;}',
							t.cssClass = "ace-itrysql";
					var r = e("../lib/dom");
					r.importCssString(t.cssText, t.cssClass)
				})