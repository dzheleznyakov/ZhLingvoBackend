export const minLength = length => ref => {
    if (!ref || !ref.current)
        return false;
    
    return ref.current.value.trim().length >= length;
};

export const notEmpty = () => minLength(1);
