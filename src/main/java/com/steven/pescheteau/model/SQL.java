package com.steven.pescheteau.model;

import com.steven.pescheteau.domain.Searchable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by steve on 01/10/2016.
 */
public class SQL {

    public static final int INT = 1;
    public static final int STRING = 2;

    private Logger LOG = LoggerFactory.getLogger(SQL.class);
    private StringBuilder request;
    private boolean paramed;
    private boolean whered;
    private boolean opened;

    public SQL(){
        super();
        request = new StringBuilder();
        paramed = false;
        whered = false;
        opened = false;
    }

    public SQL select(){
        request.append("SELECT ");
        return this;
    }

    public SQL from(){
        paramed = false;
        request.append(" FROM");
        return this;
    }

    public SQL table(Searchable obj){
        if (!paramed)
            paramed = true;
        else
            request.append(",");

        request.append(" " + obj.getTable());

        return this;
    }

    public SQL where(){
        if (!whered) {
            request.append(" WHERE ");
            paramed = false;
            whered = true;
        }
        else
            and();
        return this;
    }

    public SQL param(String param){
        if (!paramed)
            paramed = true;
        else
            request.append(", ");

        request.append(param);
        return this;
    }

    public SQL equals(){
        request.append(" = ");
        return this;
    }

    public SQL value(String value, int type){
        switch(type) {
            case STRING:
                request.append("\"").append(value.replaceAll("\"", "\"\"")).append("\"");
                break;
            case INT:
                request.append(value);
                break;
            default:
                request.append(value);
                break;
        }
        return this;
    }

    public SQL parenthese(){
        if (opened)
            request.append(" )");
        else
            request.append("( ");
        opened = !opened;
        return this;
    }

    public SQL and(){
        request.append(" AND ");
        paramed = false;
        return this;
    }

    public SQL or(){
        request.append(" OR ");
        paramed = false;
        return this;
    }

    public SQL orderBy(){
        paramed = false;
        request.append(" ORDER BY ");
        return this;
    }

    public SQL asc(){
        request.append(" ASC");
        return this;
    }

    public SQL desc(){
        request.append(" DESC");
        return this;
    }

    public SQL limit(int num){
        request.append(" LIMIT ").append(num);
        return this;
    }

    @Override
    public String toString(){
        return request.toString();
    }
}
