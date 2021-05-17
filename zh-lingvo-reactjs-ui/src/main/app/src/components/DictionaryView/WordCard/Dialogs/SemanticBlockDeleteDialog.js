import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Dialog } from '../../../UI';
import { SemanticBlock } from '../WordView/SubWordParts';
import Excerpt from './Excerpt';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const SemanticBlockDeleteDialog = () => {
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const semBlock = useSelector(selectors.stringPropertyToUpdateSelectorFactory(wordEditPath));
    const wordEditParentPath = wordEditPath.slice(0, wordEditPath.length - 1);
    const allSemBlocks = useSelector(selectors.arrayPropertyToUpdateSelectorFactory(wordEditParentPath));
    const index = wordEditPath[wordEditPath.length - 1];
    const dispatch = useDispatch();

    const onConfirm = () => {
        const updatedSemBlocks = [...allSemBlocks];
        updatedSemBlocks.splice(index, 1);
        dispatch(actions.updateWordElement(wordEditParentPath, updatedSemBlocks));
    };

    const excerpt = semBlock && (
        <Excerpt>
            <SemanticBlock path={wordEditPath} index={+index} semBlock={semBlock} editable={false} />
        </Excerpt>
    );

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
            confirmed={onConfirm}
        >
            Are you sure you want to delete this semantic block?
            {excerpt}
        </Dialog>
    );
};

export default SemanticBlockDeleteDialog;
