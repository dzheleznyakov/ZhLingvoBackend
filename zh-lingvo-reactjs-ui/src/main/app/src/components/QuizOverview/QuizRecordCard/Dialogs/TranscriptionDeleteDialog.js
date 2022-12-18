import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';
import { Dialog, Excerpt } from '../../../UI';
import { Transcription } from '../parts';

const TranscriptionDeleteDialog = () => {
    const dispatch = useDispatch();
    const path = useSelector(selectors.quizRecordEditPathSelector);
    const transcription = useSelector(selectors.quizRecordStringPropertyToUpdateSelectorFactory(path));

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowQuizRecordEditModal(false))}
            confirmed={() => dispatch(actions.updateQuizRecordElement(path, null))}
        >
            Are you sure you want to delete this transcription?
            <Excerpt>
                <Transcription>{transcription}</Transcription>
            </Excerpt>
        </Dialog>
    );
};

export default TranscriptionDeleteDialog;
