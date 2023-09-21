import * as keysMap from './keyMaps';
import * as diacriticMap from './diacriticMaps';

const remappingHandlerSupplier = (langCode, listeners, setValue) => (event) => {
    const { code } = event;
    const shouldRunDefault = 
        code === 'ArrowLeft'
        || code === 'ArrowRight'
        || code === 'ArrowUp'
        || code === 'ArrowDown'
        || code === 'ShiftLeft'
        || code === 'ShiftRight'
        || code === 'ControlLeft'
        || code === 'ControlRight'
        || code === 'MetaLeft'
        || code === 'MetaRight'
        || code === 'Tab'
        || code === 'CapsLock'
        || code === 'Delete'
        || code === 'Escape';
    let p1, p3;
    if (code === 'Backspace') {
        remapBackspace(event, setValue);
    } else if (code === 'AltLeft' || code === 'AltRight') {
        event.preventDefault();
    } else if (code === 'Delete') {
        remapDelete(event, setValue);
    } else if (!shouldRunDefault) {
        event.preventDefault();
        setValue(v => {
            const remapper = keyboardRemapper(langCode);
            const diacRemapper = diacriticRemapper(langCode);
            const { selectionStart, selectionEnd } = event.target
            let nextChar = remapper(event) || '';
            const prevChar = selectionStart === 0 ? null : v.charAt(selectionStart - 1);
            const diac = diacRemapper(nextChar, prevChar);
            p1 = diac == null
                ? v.substring(0, selectionStart)
                : v.substring(0, selectionStart - 1);
            nextChar = diac || nextChar;
            p3 = v.substring(selectionEnd - 1 + 1);
            return p1 + nextChar + p3;
        });
    }

    const { onKeyDown } = listeners;
    onKeyDown && onKeyDown(event);
};

const remapBackspace = (event, setValue) => {
    const { selectionStart, selectionEnd } = event.target
    let p1, p2, p3;
    setValue(v => {
        if (selectionStart !== selectionEnd) {
            p1 = v.substring(0, selectionStart);
            p3 = v.substring(selectionEnd - 1 + 1);
            return p1 + p3;
        } 
        if (selectionStart === 0) {
            return v;
        }
        p1 = v.substring(0, selectionStart - 1);
        p2 = v.substring(selectionStart);
        return p1 + p2;
    });
};

const remapDelete = (event, setValue) => {
    const { selectionStart, selectionEnd } = event.target
    let p1, p2, p3;
    setValue(v => {
        if (selectionStart !== selectionEnd) {
            p1 = v.substring(0, selectionStart);
            p3 = v.substring(selectionEnd - 1 + 1);
            return p1 + p3;
        } 
        if (selectionEnd === v.length) {
            return v;
        }
        p1 = v.substring(0, selectionEnd);
        p2 = v.substring(selectionEnd + 1);
        return p1 + p2;
    });
};

const keyboardRemapper = langCode => (event) => {
    const map = keysMap[langCode] || keysMap['En'];
    const values = map[event.code];
    if (values == null) return null;
    if (event.shiftKey && event.altKey) return values[3];
    if (event.shiftKey) return values[1];
    if (event.altKey) return values[2];
    else return values[0];
};

const diacriticRemapper = langCode => (char, prevChar) => {
    const map = diacriticMap[langCode];
    if (map == null) return null;
    if (map[prevChar] == null) return null;
    return map[prevChar][char] || null;
};

export default remappingHandlerSupplier;