import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import * as actions from '../../../../store/actions';
import { Dialog, Excerpt } from '../../../UI';
import { objectPropertyToUpdateSelectorFactory, wordEditPathSelector } from '../../../../store/selectors';
import { Meaning } from '../WordView/SubWordParts';
import { MEANING_TO_QUIZ_RECORD__CHOOSE_QUIZ } from '../../../../static/constants/wordEditModalTypes';

const MeaningToQuizRecordDialog_ConfirmRecord = () => {
    const dispatch = useDispatch();
    const path = useSelector(wordEditPathSelector);
    const meaning = useSelector(objectPropertyToUpdateSelectorFactory(path));

    const onClosed = () => 
        dispatch(actions.shouldShowWordEditModal(false));
    const onConfirmed = () => 
        dispatch(actions.setWordEditModalType(MEANING_TO_QUIZ_RECORD__CHOOSE_QUIZ, path));

    return meaning.id != null && (
        <Dialog
            close={onClosed}
            confirmed={onConfirmed}
            chained
        >
            Are you sure you want to create the following quiz record?
            <Excerpt>
                <Meaning 
                    meaning={meaning} 
                    path={path} 
                    editable={false} 
                />
            </Excerpt>
        </Dialog>
    );
};

export default MeaningToQuizRecordDialog_ConfirmRecord;
