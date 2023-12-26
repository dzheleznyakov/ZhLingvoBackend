package zh.args;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static zh.args.ArgsException.ErrorCode.MISSING_STRING;

public class StringArrayArgumentMarshaler implements ArgumentMarshaler {
  private final List<String> strings = new ArrayList<>();

  public void set(Iterator<String> currentArgument) throws ArgsException {
    try {
      strings.add(currentArgument.next());
    } catch (NoSuchElementException e) {
      throw new ArgsException(MISSING_STRING);
    }
  }

  public static String[] getValue(ArgumentMarshaler am) {
    if (am instanceof StringArrayArgumentMarshaler)
      return ((StringArrayArgumentMarshaler) am).strings.toArray(new String[0]);
    else
      return new String[0];
  }
}
