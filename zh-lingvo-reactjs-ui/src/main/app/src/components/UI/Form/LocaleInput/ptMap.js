const ptMap = {
    'KeyA': ['a', 'A', 'å', 'Å'],
    'KeyB': ['b', 'B', '∫', 'ı'],
    'KeyC': ['c', 'C', 'ç', 'Ç'],
    'KeyD': ['d', 'D', '∂', 'Î'],
    'KeyE': ['e', 'E', '´', '´'],
    'KeyF': ['f', 'F', 'ƒ', 'Ï'],
    'KeyG': ['g', 'G', '©', '˝'],
    'KeyH': ['h', 'H', '˙', 'Ó'],
    'KeyI': ['i', 'I', 'ˆ', 'ˆ'],
    'KeyJ': ['j', 'J', '∆', 'Ô'],
    'KeyK': ['k', 'K', '˚', ''],
    'KeyL': ['l', 'L', '¬', 'Ò'],
    'KeyM': ['m', 'M', 'µ', 'Â'],
    'KeyN': ['n', 'N', '˜', '˜'],
    'KeyO': ['o', 'O', 'ø', ''],
    'KeyP': ['p', 'P', 'π', '∏'],
    'KeyQ': ['q', 'Q', 'œ', 'Œ'],
    'KeyR': ['r', 'R', '®', '‰'],
    'KeyS': ['s', 'S', 'ß', 'Í'],
    'KeyT': ['t', 'T', '†', 'ˇ'],
    'KeyU': ['u', 'U', '¨', '¨'],
    'KeyV': ['v', 'V', '√', '◊'],
    'KeyW': ['w', 'W', '∑', '„'],
    'KeyX': ['x', 'X', '≈', '˛'],
    'KeyY': ['y', 'Y', '¥', 'Á'],
    'KeyZ': ['z', 'Z', 'Ω', '¸'],
    'IntlBackslash': ['`', '˜', '`', '`'],
    'Comma': [',', '<', '≤', '¯'],
    'Period': ['.', '>', '≥', '˘'],
    'Slash': ['/', '?', '÷', '¿'],
    'Backslash': ["\\", '|', '«', '»'],
    'Semicolon': [';', ':', '…', 'Ú'],
    'Quote': ["'", '"', 'æ', 'Æ'],
    'BracketLeft': ['[', '{', '“', '”'],
    'BracketRight': [']', '}', '‘', '’'],
    'Backquote': ['§', '±', '', ''],
    'Digit1': ['1', '!', '¡', '⁄'],
    'Digit2': ['2', '@', '€', '™'],
    'Digit3': ['3', '£', '#', '‹'],
    'Digit4': ['4', '$', '¢', '›'],
    'Digit5': ['5', '%', '∞', 'ﬁ'],
    'Digit6': ['6', '^', '§', 'ﬂ'],
    'Digit7': ['7', '&', '¶', '‡'],
    'Digit8': ['8', '*', '•', '°'],
    'Digit9': ['9', '(', 'ª', '·'],
    'Digit0': ['0', ')', 'º', '‚'],
    'Minus': ['-', '_', '–', '—'],
    'Equal': ['=', '+', '≠', '±'],
    'Space': [' ', ' ', ' ', ' '],    
};

export const ptDiacriticRemapper = {
    '´': {
        'a': 'á',
        'A': 'Á',
        'o': 'ó',
        'O': 'Ó',
        'e': 'é',
        'E': 'É',
        'u': 'ú',
        'U': 'Ú',
        'i': 'í',
        'I': 'Í',
        ' ': '´',
    },
    '`': {
        'a': 'à',
        'A': 'À',
        'o': 'ò',
        'O': 'Ò',
        'e': 'è',
        'E': 'È',
        'u': 'ù',
        'U': 'Ù',
        'i': 'ì',
        'I': 'Ì',
        ' ': '`',
    },
    '¨': {
        'a': 'ä',
        'A': 'Ä',
        'o': 'ö',
        'O': 'Ö',
        'e': 'ë',
        'E': 'Ë',
        'u': 'ü',
        'U': 'Ü',
        'i': 'ï',
        'I': 'Ï',
        'y': 'ÿ',
        'Y': 'Ÿ',
        ' ': '¨',
    },
    'ˆ': {
        'a': 'â',
        'A': 'Â',
        'o': 'ô',
        'O': 'Ô',
        'e': 'ê',
        'E': 'Ê',
        'u': 'û',
        'U': 'Û',
        'i': 'î',
        'I': 'Î',
        ' ': '^',
    },
    '˜': {
        'a': 'ã',
        'A': 'Ã',
        'o': 'õ',
        'O': 'Õ',
        'n': 'ñ',
        'N': 'Ñ',
        ' ': '~',
    },
};

export default ptMap;
