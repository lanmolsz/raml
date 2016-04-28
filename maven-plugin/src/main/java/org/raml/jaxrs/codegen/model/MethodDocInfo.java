package org.raml.jaxrs.codegen.model;

import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.mulesoft.jaxrs.raml.annotation.model.IDocInfo;

public class MethodDocInfo implements IDocInfo{
    private final static Log log = new SystemStreamLog();

    private String originalComment = "";
    private String documentation = "";
    private String example = "";
    private String returnInfo = "";
    private Deque<StringPair> throwsComment = new ArrayDeque<StringPair>();
    private Deque<StringPair> paramsComment = new ArrayDeque<StringPair>();

    public String getOriginalComment() {
        return originalComment;
    }

    public String getDocumentation() {
        return documentation;
    }

    public String getExample() {
        return example;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public String getDocumentation(String paramName){
        for (StringPair pair : paramsComment) {
            if(paramName.equals(pair.getKey())) {
                return pair.getValue().trim();
            }
        }
        if(paramName.equals("@example")) {
            return this.getExample().trim();
        }
        if(paramName.startsWith("@")) {
            paramName = paramName.replace("@","");
            return getThrowComment(paramName).trim();
        }
        return "";
    }
    public String getThrowComment(String exceptionClassName){
        for (StringPair pair : throwsComment) {
            if(exceptionClassName.equals(pair.getKey())) {
                return pair.getValue();
            }
        }
        return "";
    }

    private MethodDocInfo(){}
    
    public static void buildDocComment(CtMethod<?> me){
        MethodDocInfo comment = create(me.getDocComment());
        me.setDocComment(comment.getDocumentation());
        if(me.getType().getDeclaration() != null) {
            me.getType().getDeclaration().setDocComment(comment.getReturnInfo());
        }
        for (CtTypeReference<? extends Throwable> t : me.getThrownTypes()){
            String throwsComment = comment.getThrowComment(t.getActualClass().getSimpleName());
            if(throwsComment == null) {
                throwsComment = comment.getThrowComment(t.getActualClass().getName());
            }
            if(t.getDeclaration() != null) {
                t.getDeclaration().setDocComment(throwsComment);
            }
        }
        for (CtParameter<?> p : me.getParameters()) {
            p.setDocComment(comment.getDocumentation(p.getSimpleName()));
            if(StringUtils.isEmpty(p.getDocComment())) {
                String warnMessage = MessageFormat.format("the parameter['{0}'] of method['{1}'] does not write comment",
                        me.getReference().getActualMethod(), p.getSimpleName());
                log.warn(warnMessage);
            }
        }
    }

    public static MethodDocInfo create(String comment) {
        MethodDocInfo docComment = new MethodDocInfo();
        if(StringUtils.isNotEmpty(comment)) {
            docComment.originalComment = comment;
            PreviewNextIterator<String> comments =
                    new PreviewNextIterator<String>(Arrays.asList(comment.split("\n")));
            comments.transform(new Function<String,String>() {
                public String apply(String input) {
                    if(input.contains("*")){
                        return input.replaceFirst("(\\s*)\\*(\\s*)","");
                    }
                    return input.replaceFirst("(\\s*)","");
                }
            }).filter(new Predicate<String>() {
                public boolean apply(String input) {
                    return StringUtils.isNotBlank(input);
                }
            });
            CommentHandlers.MAIN.process(comments,docComment);
        }else{
            log.warn("Invalid doc comment:" + comment);
        }
        return docComment;
    }

    private static class StringPair {
        final String key;
        String value;
        StringPair(String key) {
            this(key,"");
        }
        StringPair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public final String getKey() {
            return key;
        }

        public final String getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final void appendValue(String append) {
            value += append;
        }
    }


    private static class PreviewNextIterator<E> implements Iterator<E> {

        private Iterator<? extends E> iterator;

        private Deque<E> items = new ArrayDeque<E>();

        public PreviewNextIterator(List<E> elements) {
            this.iterator = elements.iterator();
        }

        public PreviewNextIterator<E> transform(Function<E,E> func) {
            List<E> elements = new ArrayList<E>();
            while (hasNext()) {
                E element = next();
                element = func.apply(element);
                if(StringUtils.isNotBlank(element.toString())) {
                    elements.add(element);
                }
            }
            this.iterator = elements.iterator();
            return this;
        }

        public PreviewNextIterator<E> filter(Predicate<E> predicate){
            List<E> elements = new ArrayList<E>();
            while (hasNext()) {
                E element = next();
                if(predicate.apply(element)) {
                    elements.add(element);
                }
            }
            this.iterator = elements.iterator();
            return this;
        }

        public boolean hasNext() {
            return !items.isEmpty() || iterator.hasNext();
        }

        public E next() {
            return items.isEmpty() ? iterator.next() : items.pop();
        }

        public E previewNext() {
            E e = next();
            items.push(e);
            return e;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static interface CommentHandler {
        void process(PreviewNextIterator<String> parts,MethodDocInfo docComment);
    }

    private static enum CommentHandlers implements CommentHandler {
        MAIN("@main"),
        EXAMPLE("@example"){
            public void process(PreviewNextIterator<String> parts, MethodDocInfo docComment) {
                String line = parts.next();
                if(line.startsWith(code)) {
                    line = trimCode(line);
                }
                docComment.example += processFormat(line);
            }
        },
        PARAM("@param"){
            public void process(PreviewNextIterator<String> parts, MethodDocInfo docComment) {
                String line = parts.next();
                if(line.startsWith(code)) {
                    String[] p = parse(line);
                    line = p[1];
                	docComment.paramsComment.push(new StringPair(p[0]));
                }
                docComment.paramsComment.getFirst().appendValue(processFormat(line));
            }
        },
        RETURN ("@return"){
            public void process(PreviewNextIterator<String> parts, MethodDocInfo docComment) {
                String line = parts.next();
                if(line.startsWith(code)) {
                    line = trimCode(line);
                }
                docComment.returnInfo += processFormat(line);
            }
        },
        THROWS ("@throws"){
            public void process(PreviewNextIterator<String> parts, MethodDocInfo docComment) {
                String line = parts.next();
                if(line.startsWith(code)) {
                	String[] p = parse(line);
                    line = p[1];
                	docComment.throwsComment.push(new StringPair(p[0]));
                }
                docComment.throwsComment.getFirst().appendValue(processFormat(line));
            }
        };
        protected static CommentHandler current = MAIN;

        protected final String code;
        private CommentHandlers(String code) {
            this.code = code;
        }
        
        protected String processFormat(String line) {
        	return StringUtils.isBlank(line) ? "" : line;
        }
        
        protected String trimCode(String line){
        	return line.replace(code, "").trim() + "\n";
        }
        
        protected String[] parse(String line){
        	line = trimCode(line);
        	int position = line.indexOf(" ");
        	if(position != -1) {
        		return new String[]{line.substring(0 ,position),line.substring(position)};
        	} else {
        		return new String[]{line.trim(),""};
        	}
        }
        
        public CommentHandler getHandler(String line) {
            for (CommentHandlers handler : values()) {
                if(line.startsWith(handler.code)) {
                    return (current = handler);
                }
            }
            return current;
        }

        public synchronized void process(PreviewNextIterator<String> parts, MethodDocInfo docComment){
            while(parts.hasNext()) {
                CommentHandler handler = getHandler(parts.previewNext());
                if(handler == MAIN) {
                    docComment.documentation +=  parts.next();
                } else {
                    handler.process(parts, docComment);
                }
            }
            current = MAIN;
        }
    }
}