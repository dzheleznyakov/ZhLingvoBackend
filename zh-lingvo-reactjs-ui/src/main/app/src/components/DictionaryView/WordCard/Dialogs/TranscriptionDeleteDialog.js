import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Dialog } from '../../../UI';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const TranscriptionDeleteDialog = () => {
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const transcription = useSelector(selectors.transcriptionToUpdateSelectorFactory(wordEditPath));
    const dispatch = useDispatch();

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
            confirmed={() => dispatch(actions.updateWordElement(wordEditPath, null))}
        >
            Are you sure you want to delete transcription [{transcription}]?
        </Dialog>
    );
};

export default TranscriptionDeleteDialog;
