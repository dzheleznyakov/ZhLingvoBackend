import React from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import { exampleType } from '../../wordTypes';
import Editing from '../Editing';
import Example from './Example';
import { EXAMPLE_NEW, EXAMPLE_EDIT, EXAMPLE_DELETE } from '../../../../../../static/constants/wordEditModalTypes';
import { IconButton, iconButtonTypes, buttonSizes } from '../../../../../UI';
import * as actions from '../../../../../../store/actions';

export const NULL_EXAMPLE = { id: -1, expression: '', explanation: '' };

const EditableExample = props => {
    const { entry: example, path } = props;
    const dispatch = useDispatch();

    if (example === NULL_EXAMPLE)
        return (
            <IconButton
                type={iconButtonTypes.NEW}
                size={buttonSizes.SMALL}
                clicked={() => {
                    dispatch(actions.shouldShowWordEditModal(true));
                    dispatch(actions.setWordEditModalType(EXAMPLE_NEW, path));
                }}
            />
        );

    return (
        <Editing
            editModalType={EXAMPLE_EDIT}
            deleteModalType={EXAMPLE_DELETE}
            path={path}
            block
        >
            <Example entry={example} />
        </Editing>
    );
};

EditableExample.propTypes = {
    entry: exampleType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string),
};

EditableExample.defaultProps = {
    path: [],
};

export default EditableExample;
