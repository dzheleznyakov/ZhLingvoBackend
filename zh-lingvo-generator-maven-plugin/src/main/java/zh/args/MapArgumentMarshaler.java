package zh.args;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static zh.args.ArgsException.ErrorCode.MALFORMED_MAP;
import static zh.args.ArgsException.ErrorCode.MISSING_MAP;

public class MapArgumentMarshaler implements ArgumentMarshaler {
  private final Map<String, String> map = new HashMap<>();

  public void set(Iterator<String> currentArgument) throws ArgsException {
    try {
      String[] mapEntries = currentArgument.next().split(",");
      for (String entry : mapEntries) {
        String[] entryComponents = entry.split(":");
        if (entryComponents.length != 2)
          throw new ArgsException(MALFORMED_MAP);
        map.put(entryComponents[0], entryComponents[1]);
      }
    } catch (NoSuchElementException e) {
      throw new ArgsException(MISSING_MAP);
    }
  }

  public static Map<String, String> getValue(ArgumentMarshaler am) {
    if (am instanceof MapArgumentMarshaler)
      return ((MapArgumentMarshaler) am).map;
    else
      return new HashMap<>();
  }
}
