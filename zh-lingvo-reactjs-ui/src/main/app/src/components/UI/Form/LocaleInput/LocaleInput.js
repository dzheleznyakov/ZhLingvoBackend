import React, { useState } from 'react';
import { useSelector } from 'react-redux';

import remappingHandlerSupplier from './remappingHandlerSupplier';
import { quizRunTargetLanguageSelector } from '../../../../store/selectors';

const LocaleInput = props => {
    const {
        id,
        defaultValue,
        disabled,
        autocomplete,
        forwardRef,
        listeners,
        validation,
        onValidate,
    } = props;
    const [value, setValue] = useState(defaultValue || '');
    const langCode = useSelector(quizRunTargetLanguageSelector);
    const remappingHandler = remappingHandlerSupplier(langCode, listeners, setValue);
    const validate = validation && validation.length ? () => {
        const errors = validation
            .filter(({ validate }) => !validate(forwardRef))
            .map(({ failureMessage }) => failureMessage);
        onValidate(errors);
    } : null;
    const onKeyUp = event => {
        const { onKeyUp } = listeners;
        onKeyUp && onKeyUp(event);
        validate && validate(event);
    };
    
    return (
        <input
            id={id}
            type="text"
            disabled={disabled}
            autoComplete={autocomplete === false ? 'off' : null}
            ref={forwardRef}
            value={value}
            onChange={() => {}}
            { ...listeners }
            onKeyDown={remappingHandler}
            onKeyUp={onKeyUp}
        />
    );
};

export default LocaleInput;
