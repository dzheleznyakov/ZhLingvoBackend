import React from 'react';
import PropTypes from 'prop-types';

import { exampleType } from '../../wordTypes';
import Editing from '../Editing';
import NewPartButton from '../NewPartButton';
import Example from './Example';
import { EXAMPLE_NEW, EXAMPLE_EDIT, EXAMPLE_DELETE } from '../../../../../../static/constants/wordEditModalTypes';

export const NULL_EXAMPLE = { id: -1, expression: '', explanation: '' };

const EditableExample = props => {
    const { entry: example, path } = props;

    if (example === NULL_EXAMPLE)
        return (
            <NewPartButton
                label="example"
                modalType={EXAMPLE_NEW}
                path={path}
                block
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
