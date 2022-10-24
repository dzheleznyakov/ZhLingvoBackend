export const minLength = length => ref => {
    if (!ref || !ref.current)
        return false;
    
    return ref.current.value.trim().length >= length;
};

export const notEmpty = () => minLength(1);

export const minValue = value => ref => {
    if (!ref || !ref.current)
        return false;

    return ref.current.value >= value;
};
