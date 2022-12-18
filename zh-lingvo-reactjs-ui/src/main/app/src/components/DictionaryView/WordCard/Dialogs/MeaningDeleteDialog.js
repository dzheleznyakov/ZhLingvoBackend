import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Dialog, Excerpt } from '../../../UI';
import { Meaning } from '../WordView/SubWordParts';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const MeaningDeleteDialog = () => {
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const meaning = useSelector(selectors.stringPropertyToUpdateSelectorFactory(wordEditPath));
    const wordEditParentPath = wordEditPath.slice(0, wordEditPath.length - 1);
    const meanings = useSelector(selectors.arrayPropertyToUpdateSelectorFactory(wordEditParentPath));
    const index = wordEditPath[wordEditPath.length - 1];
    const dispatch = useDispatch();

    const onConfirm = () => {
        const updatedMeanings = [...meanings];
        updatedMeanings.splice(index, 1);
        dispatch(actions.updateWordElement(wordEditParentPath, updatedMeanings));
    };

    const excerpt = meaning && (
        <Excerpt>
            <Meaning meaning={meaning} path={wordEditPath} editable={false} />
        </Excerpt>
    );

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
            confirmed={onConfirm}
        >
            Are you sure you want to delete this word meaning?
            {excerpt}
        </Dialog>
    );
};

export default MeaningDeleteDialog;
