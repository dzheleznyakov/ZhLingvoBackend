import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';

const TranslationEditDialog = props => {
    const { editing } = props;
    const path = useSelector(selectors.quizRecordEditPathSelector);
    const translation = useSelector(
        selectors.quizRecordObjectPropertyToUpdateSelectorFactory(path));
    const translationsPath = path.slice(0, path.length - 1);
    const translations = useSelector(
        selectors.quizRecordArrayPropertyToUpdateSelectorFactory(translationsPath));
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

    const onConfirmed = () => {
        const updatedValue = translationRef.current.value.trim();
        const updatedElaboration = elaborationRef.current.value.trim() || null;
        const updatedTranslation = {
            ...translation,
            value: updatedValue,
            elaboration: updatedElaboration,
        };
        if (updatedTranslation.id == null)
            updatedTranslation.id = -1;
        if (editing)
            dispatch(actions.updateQuizRecordElement(path, updatedTranslation));
        else
            dispatch(actions.updateQuizRecordElement(translationsPath, [...translations, updatedTranslation]));
    };

    return (
        <Form
            groups={[translationGroup]}
            fields={[translationField, elaborationField]}
            canceled={() => dispatch(actions.shouldShowQuizRecordEditModal(false))}
            confirmed={onConfirmed}
        />
    );
};

TranslationEditDialog.propTypes = {
    editing: PropTypes.bool,
};

TranslationEditDialog.defaultProps = {
    editing: false,
};

export default TranslationEditDialog;
