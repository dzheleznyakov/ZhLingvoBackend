import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';

import classes from './Form.module.scss';

import { groupType, fieldType } from './formTypes';
import { Dialog, Error } from '..';
import Input from './Input';

const DEFAULT_GROUP_KEY = '###DEFAULT_GROUP_KEY###';

const processFields = (fields, fieldsByGroupKey, validationsByFieldKey) => {
    for (let i = 0, l = fields.length; i < l; ++i) {
        const field = fields[i];
        const { groupKey = DEFAULT_GROUP_KEY } = field;

        if (!fieldsByGroupKey[groupKey])
            fieldsByGroupKey[groupKey] = [];
        fieldsByGroupKey[groupKey].push(field);

        const { validation = [], key } = field;
        if (validation.length)
            validationsByFieldKey[key] = validation;
    }
};

const getFieldEntries = (field, groupKey, onValidateByKey, errors) => {
    const {
        key,
        label,
        type,
        defaultValue,
        values,
        forwardRef,
        disabled,
        validation,
    } = field;
    const formLabel = (
        <label key={`${groupKey}-${key}-label`}>{label}:</label>
    );
    const input = <Input
        id={key}
        key={`${groupKey}-${key}-input`}
        type={type}
        defaultValue={defaultValue}
        forwardRef={forwardRef}
        values={values}
        disabled={disabled}
        validation={validation}
        onValidate={onValidateByKey(key)} />;
    return [formLabel, input];
};

const Form = props => {
    const { fields, groups, canceled, confirmed } = props;
    const [errors, setErrors] = useState({});
    const [valid, setValid] = useState(false);

    useEffect(() => {
        const newValid = Object.keys(errors)
            .map(key => errors[key])
            .filter(messages => messages.length)
            .length === 0;
        setValid(newValid);
    }, [setValid, errors]);

    const fieldsByGroupKey = {};
    const validationsByFieldKey = {};
    processFields(fields, fieldsByGroupKey, validationsByFieldKey);

    const onValidateByKey = key => failureMessages => {
        const updatedErrors = { ...errors, [key]: failureMessages };
        setErrors(updatedErrors)
    }
    
    const getFields = groupKey => (fieldsByGroupKey[groupKey] || [])
        .map(field => getFieldEntries(field, groupKey, onValidateByKey, errors))
        .reduce((flatArr, arr) => flatArr.concat(arr), []);

    const defaultGroup = (
        <div 
            key="default-gr"
            className={[classes.FieldsWrapper, classes.DefaultFieldsWrapper].join(' ')}
        >
            {getFields(DEFAULT_GROUP_KEY)}
        </div>
    );
    const fieldGroups = groups.map(({ key, label }) => (
        <fieldset key={`field-gr-${key}`} className={classes.Fields}>
            <legend>{label}</legend>
            <div className={classes.FieldsWrapper}>
                {getFields(key)}
            </div>
        </fieldset>
    ));

    const errorsComponent = Object.keys(errors)
        .map(key => errors[key])
        .reduce((flat, arr) => flat.concat(arr), [])
        .map((m, i) => <Error key={`err-${i}`} message={m} />);

    return (
        <Dialog close={canceled} confirmed={confirmed} disabled={!valid}>
            <form>
                {[defaultGroup, ...fieldGroups]}
            </form>
            {errorsComponent}
        </Dialog>
    );
};

Form.propTypes = {
    fields: PropTypes.arrayOf(fieldType),
    groups: PropTypes.arrayOf(groupType),
    canceled: PropTypes.func,
    confirmed: PropTypes.func,
};

Form.defaultProps = {
    fields: [],
    groups: [],
    canceled: () => {},
    confirmed: () => {},
};

export default Form;
