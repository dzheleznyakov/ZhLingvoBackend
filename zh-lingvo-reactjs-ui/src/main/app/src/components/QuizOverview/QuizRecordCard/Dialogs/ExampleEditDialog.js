import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';

const ExampleEditDialog = props => {
    const { editing } = props;
    const path = useSelector(selectors.quizRecordEditPathSelector);
    const example = useSelector(selectors.quizRecordObjectPropertyToUpdateSelectorFactory(path));
    const examplesPath = path.slice(0, path.length - 1);
    const examples = useSelector(
        selectors.quizRecordArrayPropertyToUpdateSelectorFactory(examplesPath));
    const { remark, expression, explanation } = example;
    const dispatch = useDispatch();

    const exampleGroup = {
        key: 'exampleGroup',
        label: editing ? 'Update Example' : 'New Example',
    };

    const remarkRef = useRef();
    const remarkField = {
        key: 'remarkField',
        label: 'Remark',
        type: formInputTypes.TEXT,
        defaultValue: remark || '',
        autocomplete: false,
        groupKey: exampleGroup.key,
        forwardRef: remarkRef,
    };

    const expressionRef = useRef();
    const expressionField = {
        key: 'expressionField',
        label: 'Expression',
        type: formInputTypes.TEXT,
        defaultValue: expression || '',
        autocomplete: false,
        groupKey: exampleGroup.key,
        forwardRef: expressionRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Expression cannot be empty',
        }],
    };

    const explanationRef = useRef();
    const explanationField = {
        key: 'explanationField',
        label: 'Explanation',
        type: formInputTypes.TEXT,
        defaultValue: explanation || '',
        autocomplete: false,
        groupKey: exampleGroup.key,
        forwardRef: explanationRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Explanation cannot be empty',
        }],
    };

    useAutofocus(remarkRef);

    const onConfirmed = () => {
        const updatedRemark = remarkRef.current.value.trim() || null;
        const updatedExpression = expressionRef.current.value.trim();
        const updatedExplanation = explanationRef.current.value.trim();
        const updatedExample = {
            ...example,
            remark: updatedRemark,
            expression: updatedExpression,
            explanation: updatedExplanation,
        };
        if (!updatedExample.id)
            updatedExample.id = -1;
        if (editing)
            dispatch(actions.updateQuizRecordElement(path, updatedExample));
        else
            dispatch(actions.updateQuizRecordElement(examplesPath, [...examples, updatedExample]));
    };

    const onCanceled = () => dispatch(actions.shouldShowQuizRecordEditModal(false));

    return (
        <Form
            groups={[exampleGroup]}
            fields={[remarkField, expressionField, explanationField]}
            canceled={onCanceled}
            confirmed={onConfirmed}
        />
    );
};

ExampleEditDialog.propTypes = {
    editing: PropTypes.bool,
};

ExampleEditDialog.defaultProps = {
    editing: false,
};

export default ExampleEditDialog;
