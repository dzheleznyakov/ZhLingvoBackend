import React from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './SemanticBlock.module.scss';

import { EditableMeaning, NULL_MEANING } from '.';
import { semBlockType } from '../wordTypes';
import * as selectors from '../../../../../store/selectors';

const SemanticBlock = props => {
    const { semBlock, index, path } = props;
    const { pos } = semBlock;
    const isEditing = useSelector(selectors.isEditingSelector);

    const nullMeaning = isEditing ? [NULL_MEANING] : []
    const meanings = (semBlock.meanings || []).concat(nullMeaning)
        .map((m, i) => (
            <li className={classes.MeaningItem} key={m.id}>
                <EditableMeaning 
                    path={[...path, 'meanings', `${i}`]} 
                    meaning={m} 
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
};

export default SemanticBlock;
