import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Dialog, Excerpt } from '../../../UI';
import { Transcription } from '../WordView/SubWordParts';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const TranscriptionDeleteDialog = () => {
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const transcription = useSelector(selectors.stringPropertyToUpdateSelectorFactory(wordEditPath));
    const dispatch = useDispatch();

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
            confirmed={() => dispatch(actions.updateWordElement(wordEditPath, null))}
        >
            Are you sure you want to delete this transcription?
            <Excerpt>
                <Transcription>{transcription}</Transcription>
            </Excerpt>
        </Dialog>
    );
};

export default TranscriptionDeleteDialog;
