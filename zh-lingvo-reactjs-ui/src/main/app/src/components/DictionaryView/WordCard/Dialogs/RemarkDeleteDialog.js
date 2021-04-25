import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Dialog } from '../../../UI';
import { Remark } from '../WordView/SubWordParts';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const RemarkDeleteDialog = () => {
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const remark = useSelector(selectors.stringPropertyToUpdateSelectorFactory(wordEditPath));
    const dispatch = useDispatch();

    return (
        <Dialog
            close={() => dispatch(actions.shouldShowWordEditModal(false))}
            confirmed={() => dispatch(actions.updateWordElement(wordEditPath, null))}
        >
            Are you sure you want to delete this remark?
            <blockquote>
                <Remark value={remark} />
            </blockquote>
        </Dialog>
    );
};

export default RemarkDeleteDialog;
