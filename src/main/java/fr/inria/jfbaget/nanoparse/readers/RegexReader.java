package fr.inria.jfbaget.nanoparse.readers;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.Match;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;

public class RegexReader extends AbstractReader {

    public Pattern pattern;

    public RegexReader(String identifier, String pattern, boolean skip) throws PatternSyntaxException {
        super(identifier, skip);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    protected IMatch simpleread(CharSequence input, int start) {
    	Matcher matcher = this.pattern.matcher(input).region(start, input.length());
        if (matcher.lookingAt()) {
            return this.makeMatch(start, matcher.end(), true, matcher.group(0));
        }
        else {
            return new StringMatch(this, start, start, false, null);
        }
    }
    
    @Override
    protected void simpleLink(IParser parser) {
    	
    }
    
    // Attention, ne peut être appelé que sur un succés
    protected IMatch makeMatch(int start, int end, boolean success, String result) {
    	return new StringMatch(this, start, end, success, result);
    }
    
}
