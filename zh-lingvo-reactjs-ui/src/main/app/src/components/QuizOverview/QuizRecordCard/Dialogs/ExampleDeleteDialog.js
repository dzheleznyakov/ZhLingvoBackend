import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import * as selectors from '../../../../store/selectors';
import * as actions from '../../../../store/actions';
import { Dialog, Excerpt } from '../../../UI';
import { QuizRecordExample } from '../parts';

const ExampleDeleteDialog = () => {
    const path = useSelector(selectors.quizRecordEditPathSelector);
    const example = useSelector(selectors.quizRecordObjectPropertyToUpdateSelectorFactory(path));
    const examplesPath = path.slice(0, path.length - 1);
    const examples = useSelector(
        selectors.quizRecordArrayPropertyToUpdateSelectorFactory(examplesPath));
    const index = path[path.length - 1];
    const dispatch = useDispatch();

    const onClosed = () =>
        dispatch(actions.shouldShowQuizRecordEditModal(false));

    const onConfirmed = () => {
        const updatedExamples = [...examples];
        updatedExamples.splice(index, 1);
        dispatch(actions.updateQuizRecordElement(examplesPath, updatedExamples));
    };

    return example.id != null && (
        <Dialog
            close={onClosed}
            confirmed={onConfirmed}
        >
            Are you sure you want to delete this example?
            <Excerpt>
                <QuizRecordExample entry={example} />
            </Excerpt>
        </Dialog>
    );
};

export default ExampleDeleteDialog;
