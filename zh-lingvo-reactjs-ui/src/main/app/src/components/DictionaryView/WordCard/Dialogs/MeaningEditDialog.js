import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { Dialog } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const MeaningEditDialog = props => {
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const wordEditParentPath = wordEditPath.slice(0, wordEditPath.length - 1);
    const meanings = useSelector(selectors.arrayPropertyToUpdateSelectorFactory(wordEditParentPath));
    const dispatch = useDispatch();

    const onConfirm = () => {
        dispatch(actions.updateWordElement(wordEditParentPath, [...meanings, {
            translations: [],
        }]));
    };

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
            confirmed={onConfirm}
        >
            Are you sure you want to add a new meaning?
        </Dialog>
    );
};

export default MeaningEditDialog;
