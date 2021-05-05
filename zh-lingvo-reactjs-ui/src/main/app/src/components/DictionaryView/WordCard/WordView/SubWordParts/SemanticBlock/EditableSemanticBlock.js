import React from 'react';
import PropTypes from 'prop-types';

import StaticEditable from '../Editable/StaticEditable';
import NewPartButton from '../NewPartButton';
import SemanticBlock from './SemanticBlock';
import { semBlockType } from '../../wordTypes';
import { SEM_BLOCK_EDIT, SEM_BLOCK_DELETE, SEM_BLOCK_NEW} from '../../../../../../static/constants/wordEditModalTypes';

export const NULL_SEMANTIC_BLOCK = { id: -1, pos: '' };

const EditableSemanticBlock = props => {
    const { semBlock, index, path } = props;

    if (semBlock === NULL_SEMANTIC_BLOCK)
        return (
            <NewPartButton
                label="..."
                modalType={SEM_BLOCK_NEW}
                path={path}
            />
        );
    
    return (
        <StaticEditable
            editModalType={SEM_BLOCK_EDIT}
            deleteModalType = {SEM_BLOCK_DELETE}
            path={path}
            block
        >
            <SemanticBlock path={path} index={index} semBlock={semBlock} />
        </StaticEditable>
    );
};

EditableSemanticBlock.propTypes = {
    index: PropTypes.number.isRequired,
    semBlock: semBlockType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default EditableSemanticBlock;
