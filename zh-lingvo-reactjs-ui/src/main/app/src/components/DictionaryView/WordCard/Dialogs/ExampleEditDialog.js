import React, { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const ExampleEditDialog = props => {
    const { editing } = props;
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const example = useSelector(selectors.objectPropertyToUpdateSelectorFactory(wordEditPath));
    const wordEditParentPath = wordEditPath.slice(0, wordEditPath.length - 1);
    const examples = useSelector(selectors.arrayPropertyToUpdateSelectorFactory(wordEditParentPath));
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
        groupKey: exampleGroup.key,
        forwardRef: remarkRef,
    };

    const expressionRef = useRef();
    const expressionField = {
        key: 'expressionField',
        label: 'Expression',
        type: formInputTypes.TEXT,
        defaultValue: expression || '',
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
        const updatedExplanation = explanationRef.current.value.trim() || null;
        const updatedExample = {
            ...example,
            remark: updatedRemark,
            expression: updatedExpression,
            explanation: updatedExplanation,
        };
        if (editing)
            dispatch(actions.updateWordElement(wordEditPath, updatedExample))
        else 
            dispatch(actions.updateWordElement(wordEditParentPath, [...examples, updatedExample]));
    };

    return <Form 
        fields={[remarkField, expressionField, explanationField]}
        groups={[exampleGroup]}
        canceled={() => dispatch(actions.shouldShowWordEditModal(false))}
        confirmed={onConfirmed}
    />;
};

ExampleEditDialog.propTypes = {
    editing: PropTypes.bool,
};

export default ExampleEditDialog;
