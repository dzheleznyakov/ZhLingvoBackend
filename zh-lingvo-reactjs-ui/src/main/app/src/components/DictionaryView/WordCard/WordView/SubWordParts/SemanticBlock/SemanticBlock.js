import React from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './SemanticBlock.module.scss';

import { Meaning, EditableMeaning, NULL_MEANING } from '..';
import { semBlockType } from '../../wordTypes';
import * as selectors from '../../../../../../store/selectors';

const MEANINGS_PATH_SEGMENT = 'meanings';

const SemanticBlock = props => {
    const { semBlock, index, path, editable } = props;
    const { pos } = semBlock;
    const isEditing = useSelector(selectors.isEditingSelector);

    const nullMeaning = editable && isEditing ? [NULL_MEANING] : []
    const MeaningComp = editable ? EditableMeaning : Meaning;
    const meanings = (semBlock.meanings || []).concat(nullMeaning)
        .map((m, i) => (
            <li className={classes.MeaningItem} key={m.id >= 0 ? m.id : -i}>
                <MeaningComp 
                    path={[...path, MEANINGS_PATH_SEGMENT, `${i}`]} 
                    meaning={m} 
                    editable={editable}
                />
            </li>
        ));


    return (
        <div>
            <div>
                <span className={classes.SemBlockEnum}>{index + 1}. </span>
                <span className={classes.PoS}>{pos}</span>
            </div>
            <div>
                <ol className={classes.MeaningsList}>
                    {meanings}
                </ol>
            </div>
        </div>
    );
};

SemanticBlock.propTypes = {
    index: PropTypes.number.isRequired,
    semBlock: semBlockType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
    editable: PropTypes.bool,
};

SemanticBlock.defaultProps = {
    editable: true,
}

export default SemanticBlock;
