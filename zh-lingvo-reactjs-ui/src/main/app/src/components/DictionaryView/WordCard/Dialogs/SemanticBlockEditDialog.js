import React, { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const SemanticBlockEditDialog = props => {
    const { editing } = props;
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const semBlock = useSelector(selectors.objectPropertyToUpdateSelectorFactory(wordEditPath));
    const wordEditParentPath = wordEditPath.slice(0, wordEditPath.length - 1);
    const allSemBlocks = useSelector(selectors.arrayPropertyToUpdateSelectorFactory(wordEditParentPath));
    const { pos } = semBlock;
    const dispatch = useDispatch();

    const semBlockGroup = {
        key: 'semBlockGroup',
        label: editing ? 'Update Semantic Block' : 'New Semantic Block',
    };

    const posRef = useRef();
    const posField = {
        key: 'posField',
        label: 'Part of Speech',
        type: formInputTypes.SELECT,
        defaultValue: pos || '',
        values: ['v', 'n', 'adj'],
        groupKey: semBlockGroup.key,
        forwardRef: posRef,
    };

    useAutofocus(posRef);

    const onConfirmed = () => {
        const updatedPos = posRef.current.value.trim() || null;
        const updatedSemBlock = {
            ...semBlock,
            pos: updatedPos,
        };
        if (editing)
            dispatch(actions.updateWordElement(wordEditPath, updatedSemBlock))
        else 
            dispatch(actions.updateWordElement(wordEditParentPath, [...allSemBlocks, updatedSemBlock]));
    };

    return <Form 
        fields={[posField]}
        groups={[semBlockGroup]}
        canceled={() => dispatch(actions.shouldShowWordEditModal(false))}
        confirmed={onConfirmed}
    />;
};

SemanticBlockEditDialog.propTypes = {
    editing: PropTypes.bool,
};

export default SemanticBlockEditDialog;
