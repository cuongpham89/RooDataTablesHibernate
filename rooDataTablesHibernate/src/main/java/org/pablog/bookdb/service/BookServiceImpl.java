package org.pablog.bookdb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.Interval;
import org.pablog.bookdb.domain.Book;
import org.pablog.bookdb.utils.ExcludeDatePropertiesSelector;
import org.pablog.bookdb.web.datatables.DataTablesRequest;
import org.pablog.bookdb.web.datatables.DataTablesResponse;
import org.pablog.bookdb.web.datatables.IntervalFactory;

import flexjson.JSONDeserializer;

public class BookServiceImpl implements BookService {

    public DataTablesResponse<Book> datatablesDraw(String json) throws Exception {
        List<Book> data = new ArrayList<Book>();
        DataTablesRequest<Book> dataTablesRequest = new JSONDeserializer<DataTablesRequest<Book>>()
        		.use("msIntervals", new IntervalFactory("dd/MM/yyyy"))
        		.use("searchObj", Book.class)
//        		.use("extraData", FindBookBean.class)
        		.use(null, DataTablesRequest.class)
        		.deserialize(json);
        
        /* 
         * Default order by datePublished desc. 
         */
        if(dataTablesRequest.aiSortCol == null){
        	dataTablesRequest.aiSortCol = new ArrayList<Integer>();
        	dataTablesRequest.aiSortCol.add(new Integer(0));
        	dataTablesRequest.amDataProp.set(0,"datePublished");
        	dataTablesRequest.asSortDir = new ArrayList<String>();
        	dataTablesRequest.asSortDir.add("desc");
        }
        
        data = findByExampleLike(dataTablesRequest);
        
        Long totalRecords = countByExampleLike(dataTablesRequest);
        
        DataTablesResponse<Book> result = new DataTablesResponse<Book>(""+dataTablesRequest.sEcho, 
        		totalRecords.intValue(), 
        		totalRecords.intValue(), 
        		data, 
        		dataTablesRequest.sColumns);

	    return result;
    }
    
    
    private static List<Book> findByExampleLike(DataTablesRequest<Book> dataTablesRequest) {
    	Session session = (Session) Book.entityManager().getDelegate();
    	Book e = dataTablesRequest.searchObj;
        Example example = Example.create(e).enableLike(MatchMode.START).ignoreCase().setPropertySelector(new ExcludeDatePropertiesSelector());
        Criteria criteria = session.createCriteria(Book.class, "book").add(example);
        int firstResult = dataTablesRequest.iDisplayStart;
        int maxResults = dataTablesRequest.iDisplayLength;
        String sortColumn = dataTablesRequest.amDataProp.get(dataTablesRequest.aiSortCol.get(0));
        String sortDir = dataTablesRequest.asSortDir.get(0);
        
        Map<String, Interval> msIntervals = dataTablesRequest.msIntervals;
        if (msIntervals != null && !msIntervals.isEmpty()) {
        	addDateIntervalCriterion(criteria, msIntervals);
        }
        
//        associationsCriteria(criteria, e, sortColumn, sortDir);
        criteria.setFirstResult(firstResult).setMaxResults(maxResults);
        
        return criteria.list();
    }
    
    private static long countByExampleLike(DataTablesRequest<Book> dataTablesRequest) {
    	Session session = (Session) Book.entityManager().getDelegate();
    	Book e = dataTablesRequest.searchObj;
        Example example = Example.create(e).enableLike(MatchMode.START).ignoreCase().setPropertySelector(new ExcludeDatePropertiesSelector());
        Criteria criteria = session.createCriteria(Book.class, "book").add(example);
        
        Map<String, Interval> msIntervals = dataTablesRequest.msIntervals;
        if (msIntervals != null && !msIntervals.isEmpty()) {
        	addDateIntervalCriterion(criteria, msIntervals);
        }
        
//        associationsCriteria(criteria, e, null, null);
        
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }
    
    private static void addDateIntervalCriterion(Criteria criteria, Map<String,Interval> msIntervals) {
    	for(Map.Entry<String, Interval> e : msIntervals.entrySet()) {
    		String propertyName = e.getKey();
    		Interval interval = e.getValue();
    		if(interval != null) {
    			if(interval.getStartMillis() != Long.MIN_VALUE) {
        			criteria.add(Restrictions.ge(propertyName, new Date(interval.getStartMillis())));
        		}
        		if(interval.getEndMillis() != Long.MAX_VALUE) {
        			criteria.add(Restrictions.le(propertyName, new Date(interval.getEndMillis())));
        		}    			
    		}
    	}
    }
    /*
    private static void associationsCriteria(Criteria criteria, Book e, String sortColumn, String sortDir) {
    	boolean isAssnOrder = false;
    	if(e.getProperty() != null || (sortColumn != null && sortColumn.equals("property"))) {
    		Criteria assnCrit = criteria.createCriteria("property");
    		if(e.getProperty() != null) {
    			assnCrit.add(Example.create(e.getProperty()));
    		}
    		if(sortColumn != null && sortDir != null && sortColumn.equals("property")) {
        		assnCrit.addOrder("desc".equals(sortDir) ? Order.desc("descrizione") : Order.asc("descrizione"));
        		isAssnOrder = true;
        	}
        }
        
        if(sortColumn != null && sortDir != null && !isAssnOrder) {
        	criteria.addOrder("desc".equals(sortDir) ? Order.desc(sortColumn) : Order.asc(sortColumn));
        }
    }
    */
}
