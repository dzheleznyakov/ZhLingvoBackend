import React from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import Editing from '../Editing';
import Translation from './Translation';
import { TRANSLATION_EDIT, TRANSLATION_DELETE, TRANSLATION_NEW } from '../../../../../../static/constants/wordEditModalTypes';
import { translationType } from '../../wordTypes';
import { IconButton, iconButtonTypes, buttonSizes } from '../../../../../UI';
import * as actions from '../../../../../../store/actions';

export const NULL_TRANSLATION = { id: -1, value: '' };

const EditableTranslation = props => {
    const { entry: translation, path, postfix } = props;
    const dispatch = useDispatch();

    if (translation === NULL_TRANSLATION)
        return (
            <IconButton
                type={iconButtonTypes.NEW}
                size={buttonSizes.SMALL}
                clicked={() => {
                    dispatch(actions.shouldShowWordEditModal(true));
                    dispatch(actions.setWordEditModalType(TRANSLATION_NEW, path));
                }}
            />
        );

    return (
        <Editing
            editModalType={TRANSLATION_EDIT}
            deleteModalType={TRANSLATION_DELETE}
            path={path}
        >
            <Translation entry={translation} postfix={postfix} />
        </Editing>
    );
};

EditableTranslation.propTypes = {
    entry: translationType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default EditableTranslation;
