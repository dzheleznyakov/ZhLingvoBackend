import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './DialogsCommonStyles.module.scss';

import { Dialog } from '../../../UI';
import { Example } from '../WordView/SubWordParts';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const ExampleDeleteDialog = () => {
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const example = useSelector(selectors.objectPropertyToUpdateSelectorFactory(wordEditPath));
    const wordEditParentPath = wordEditPath.slice(0, wordEditPath.length - 1);
    const examples = useSelector(selectors.arrayPropertyToUpdateSelectorFactory(wordEditParentPath));
    const index = wordEditPath[wordEditPath.length - 1];
    const dispatch = useDispatch();

    const onConfirm = () => {
        const updatedExamples = [...examples];
        updatedExamples.splice(index, 1);
        dispatch(actions.updateWordElement(wordEditParentPath, updatedExamples));
    };

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
            confirmed={onConfirm}
        >
            Are you sure you want to delete this translation?
            <blockquote className={classes.Excerpt}>
                <Example entry={example} />
            </blockquote>
        </Dialog>
    );
};

export default ExampleDeleteDialog;
