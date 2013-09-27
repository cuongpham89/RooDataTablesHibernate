package org.pablog.bookdb.service;
import org.pablog.bookdb.domain.Book;
import org.pablog.bookdb.web.datatables.DataTablesResponse;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { org.pablog.bookdb.domain.Book.class })
public interface BookService {
	public DataTablesResponse<Book> datatablesDraw(String json) throws Exception;
}
