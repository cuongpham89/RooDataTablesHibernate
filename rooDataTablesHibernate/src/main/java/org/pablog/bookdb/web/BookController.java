package org.pablog.bookdb.web;

import org.pablog.bookdb.domain.Book;
import org.pablog.bookdb.web.datatables.DataTablesResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import flexjson.JSONSerializer;

@RequestMapping("/books")
@Controller
@RooWebScaffold(path = "books", formBackingObject = Book.class)
public class BookController {
	@RequestMapping(produces = "text/html")
    public String list(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "redirect:/";
    }
	
	@RequestMapping(params = "data", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> data(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        try{
        	DataTablesResponse<Book> result = bookService.datatablesDraw(json);
        	
        	return  new ResponseEntity<String>(new JSONSerializer()
			        	.include("aaData.name")
			        	.include("aaData.description")
			        	.include("aaData.author")
			        	.include("aaData.publisher")
			        	.include("aaData.isbn")
			        	.include("aaData.datePublished")
			        	.include("aaData.id")
			        	.exclude("aaData.*")
		        		.exclude("*.class")
		//        		.transform(new IterableTransformer(), Collection.class) 
		        		.deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception ex) {
        	ex.printStackTrace();
        	return new ResponseEntity<String>(ex.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
