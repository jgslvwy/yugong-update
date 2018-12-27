package com.taobao.yugong.translator;

import com.taobao.yugong.common.db.meta.ColumnValue;
import com.taobao.yugong.common.model.record.Record;
import org.apache.commons.lang.ObjectUtils;

import java.sql.Types;

public class TCiUserDataTranslator extends AbstractDataTranslator implements DataTranslator {

    public boolean translator(Record record) {
        record.setTableName("T_CI_USER");
        //至此可知道源表为yugong_example_a，目标表为yugong_example_mysql

        // 2. 字段名字不同,
        //源表的name字段,目标表中叫display_name
        ColumnValue nameColumn = record.getColumnByName("SEQUENCE_NO");
        if (nameColumn != null) {
            nameColumn.getColumn().setName("ID");
        }
//        // 3. 字段逻辑处理
//        ColumnValue aliasNameColumn = record.getColumnByName("alias_name");
//        if (aliasNameColumn != null) {
//            StringBuilder displayNameValue = new StringBuilder(64);
//            displayNameValue.append(ObjectUtils.toString(nameColumn.getValue()))
//                    .append('(')
//                    .append(ObjectUtils.toString(aliasNameColumn.getValue()))
//                    .append(')');
//            nameColumn.setValue(displayNameValue.toString());
//        }

        // 4. 字段类型不同
        if (nameColumn != null) {
            nameColumn.getColumn().setType(Types.INTEGER);
            nameColumn.setValue(ObjectUtils.toString(nameColumn.getValue()));
        }

//        // 5. 源库多一个字段
//        record.removeColumnByName("alias_name");
//
//        // 6. 目标库多了一个字段
//        ColumnMeta gmtMoveMeta = new ColumnMeta("gmt_move", Types.TIMESTAMP);
//        ColumnValue gmtMoveColumn = new ColumnValue(gmtMoveMeta, new Date());
//        gmtMoveColumn.setCheck(false);// 该字段不做对比
//        record.addColumn(gmtMoveColumn);

        // ColumnValue text_c = record.getColumnByName("text_c");
        // try {
        // text_c.setValue(new String((byte[]) text_c.getValue(), "GBK"));
        // } catch (UnsupportedEncodingException e) {
        // e.printStackTrace();
        // }
        return super.translator(record);
    }
}
