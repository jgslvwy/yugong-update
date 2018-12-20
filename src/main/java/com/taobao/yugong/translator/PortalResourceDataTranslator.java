//package com.taobao.yugong.translator;
//import com.taobao.yugong.common.db.meta.ColumnMeta;
//import com.taobao.yugong.common.db.meta.ColumnValue;
//import com.taobao.yugong.common.model.record.Record;
//import com.taobao.yugong.translator.AbstractDataTranslator;
//import com.taobao.yugong.translator.DataTranslator;
//import org.apache.commons.lang.ObjectUtils;
//
//import java.sql.Types;
//import java.util.Date;
//
//public class PortalResourceDataTranslator extends AbstractDataTranslator implements DataTranslator {
//    public boolean translator(Record record) {
//        // 1. schema/table名不同
//        record.setSchemaName("portal");
//        record.setTableName("author_resource");
//
//        // 2. 字段名字不同
//        ColumnValue c1  = record.getColumnByName("ID");
//        if (c1 != null) {
//            c1.getColumn().setName("id");
//        }
//        ColumnValue c2 = record.getColumnByName("ACCESS_MODULE");
//        if (c2 != null) {
//            c2.getColumn().setName("access_module");
//        }
//        ColumnValue c3 = record.getColumnByName("ACCESS_PATH");
//        if (c3 != null) {
//            c3.getColumn().setName("access_path");
//        }
//
//        // 3. 字段逻辑处理
//        ColumnValue aliasNameColumn = record.getColumnByName("alias_name");
//        StringBuilder displayNameValue = new StringBuilder(64);
//        displayNameValue.append(ObjectUtils.toString(nameColumn.getValue()))
//                .append('(')
//                .append(ObjectUtils.toString(aliasNameColumn.getValue()))
//                .append(')');
//        nameColumn.setValue(displayNameValue.toString());
//
//        // 4. 字段类型不同
//        ColumnValue amountColumn = record.getColumnByName("amount");
//        amountColumn.getColumn().setType(Types.VARCHAR);
//        amountColumn.setValue(ObjectUtils.toString(amountColumn.getValue()));
//
//        // 5. 源库多一个字段
//        record.getColumns().remove(aliasNameColumn);
//
//        // 6. 目标库多了一个字段
//        ColumnMeta gmtMoveMeta = new ColumnMeta("gmt_move", Types.TIMESTAMP);
//        ColumnValue gmtMoveColumn = new ColumnValue(gmtMoveMeta, new Date());
//        record.addColumn(gmtMoveColumn);
//
//        return super.translator(record);
//    }
//
//}
///**
// 疑问:
// 1、DataTranslator动态编译
// 2、DataTranslator查找规则：根据表名自动查找
// 3、其他复杂转换：
// a、多张Oracle表和一张MySQL转换处理
// b、一张Oracle表和多张MySQL
// **/