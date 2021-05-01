import React from 'react';
import PropTypes from 'prop-types';

import StaticEditable from '../Editable/StaticEditable';
import NewPartButton from '../NewPartButton';
import { meaningType } from '../../wordTypes';
import { MEANING_NEW, MEANING_DELETE } from '../../../../../../static/constants/wordEditModalTypes';
import { Meaning } from '..';

export const NULL_MEANING = { id: -1 };

const EditableMeaning = props => {
    const { meaning, path } = props;

    if (meaning === NULL_MEANING)
        return (
            <NewPartButton
                label="..."
                modalType={MEANING_NEW}
                path={path}
            />
        );

    return (
        <StaticEditable
            deleteModalType={MEANING_DELETE}
            path={path}
            block
        >
            <Meaning path={path} meaning={meaning} />
        </StaticEditable>
    );
};

EditableMeaning.propTypes = {
    meaning: meaningType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default EditableMeaning;
