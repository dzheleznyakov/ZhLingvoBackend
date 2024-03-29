import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Dialog, Excerpt } from '../../../UI';
import { Translation } from '../WordView/SubWordParts';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const TranslationDeleteDialog = () => {
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const translation = useSelector(selectors.objectPropertyToUpdateSelectorFactory(wordEditPath));
    const wordEditParentPath = wordEditPath.slice(0, wordEditPath.length - 1);
    const translations = useSelector(selectors.arrayPropertyToUpdateSelectorFactory(wordEditParentPath));
    const index = wordEditPath[wordEditPath.length - 1];
    const dispatch = useDispatch();

    const onConfirm = () => {
        const updatedTranslations = [...translations];
        updatedTranslations.splice(index, 1);
        dispatch(actions.updateWordElement(wordEditParentPath, updatedTranslations));
    };

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
            confirmed={onConfirm}
        >
            Are you sure you want to delete this translation?
            <Excerpt>
                <Translation entry={translation} />
            </Excerpt>
        </Dialog>
    );
};

export default TranslationDeleteDialog;
