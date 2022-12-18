import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import { Dialog, Excerpt } from '../../../UI';
import { QuizRecordTranslation } from '../parts';

const TranslationDeleteDialog = () => {
    const path = useSelector(selectors.quizRecordEditPathSelector);
    const translation = useSelector(selectors.quizRecordObjectPropertyToUpdateSelectorFactory(path));
    const trnaslationsPath = path.slice(0, path.length - 1);
    const translations = useSelector(selectors.quizRecordArrayPropertyToUpdateSelectorFactory(trnaslationsPath));
    const index = +path[path.length - 1];
    const dispatch = useDispatch();

    const onConfirm = () => {
        const updatedTranslations = [...translations];
        updatedTranslations.splice(index, 1);
        dispatch(actions.updateQuizRecordElement(trnaslationsPath, updatedTranslations));
    };

    return translation.id != null && (
        <Dialog
            close={() => dispatch(actions.shouldShowQuizRecordEditModal(false))}
            confirmed={onConfirm}
        >
            Are you sure you want to delete this translations?
            <Excerpt>
                <QuizRecordTranslation entry={translation} />
            </Excerpt>
        </Dialog>
    );
};

export default TranslationDeleteDialog;
