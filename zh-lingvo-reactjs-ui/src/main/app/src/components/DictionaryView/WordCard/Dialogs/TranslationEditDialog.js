import React, { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const TranslationEditDialog = props => {
    const { editing } = props;
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const translation = useSelector(selectors.objectPropertyToUpdateSelectorFactory(wordEditPath));
    const wordEditParentPath = wordEditPath.slice(0, wordEditPath.length - 1);
    const translations = useSelector(selectors.arrayPropertyToUpdateSelectorFactory(wordEditParentPath));
    const { value, elaboration } = translation;
    const dispatch = useDispatch();

    const translationGroup = {
        key: 'translationGroup',
        label: editing ? 'Update Translation' : 'New Translation',
    };

    const translationRef = useRef();
    const translationField = {
        key: 'translationField',
        label: 'Translation',
        type: formInputTypes.TEXT,
        defaultValue: value || '',
        groupKey: translationGroup.key,
        forwardRef: translationRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Translation cannot be empty',
        }],
    };

    const elaborationRef = useRef();
    const elaborationField = {
        key: 'elaborationField',
        label: 'Elaboration',
        type: formInputTypes.TEXT,
        defaultValue: elaboration || '',
        groupKey: translationGroup.key,
        forwardRef: elaborationRef,
    };

    useAutofocus(translationRef);

    const onConfirm = () => {
        const updatedValue = translationRef.current.value.trim();
        const updatedElaboration = elaborationRef.current.value.trim() || null;
        const updatedTranslation = {
            ...translation,
            value: updatedValue,
            elaboration: updatedElaboration,
        };
        if (editing)
            dispatch(actions.updateWordElement(wordEditPath, updatedTranslation))
        else 
            dispatch(actions.updateWordElement(wordEditParentPath, [...translations, updatedTranslation]));
    };

    return <Form 
        fields={[translationField, elaborationField]}
        groups={[translationGroup]}
        canceled={() => dispatch(actions.shouldShowWordEditModal(false))}
        confirmed={onConfirm}
    />;
};

TranslationEditDialog.propTypes = {
    editing: PropTypes.bool,
};

export default TranslationEditDialog;
