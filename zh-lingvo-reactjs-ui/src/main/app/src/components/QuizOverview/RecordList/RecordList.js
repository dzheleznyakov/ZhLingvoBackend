import React, { useEffect } from 'react';
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';

import classes from './RecordList.module.scss';

import { useActionOnMount } from '../../../hooks';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';
import RecordListItem from './RecordListItem/RecordListItem';
import RecordListControl from './RecordListControl/RecordListControl';
import { ListView } from '../../UI';

const RecordList = () => {
    const { qid: quizId, rid } = useParams();
    const dispatch = useDispatch();
    const overviews = useSelector(selectors.quizRecordsOverviewsSelector);
    const isEditing = useSelector(selectors.quizRecordIsEditingSelector);

    const selectedIndex = rid !== null && rid !== undefined
        ? overviews.findIndex(overview => overview.id === +rid)
        : -1;

    useActionOnMount(actions.fetchQuizRecords(quizId));

    useEffect(() => {
        if (rid !== null && rid !== undefined) {
            const selectedQuizRecordIndex = overviews.findIndex(qRec => qRec.id === +rid);
            dispatch(actions.selectQuizRecord(selectedQuizRecordIndex));
        }
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    const wrapperClasses = [classes.QuizRecordListWrapper];
    if (rid !== null && rid !== undefined)
        wrapperClasses.push(classes.Active);

    const items = overviews.map(overview => 
        ({ key: `${overview.id}`, node: <RecordListItem quizRecord={overview} /> }));

    const onRecordClick = index => () => {
        dispatch(actions.selectQuizRecord(index));
        const recordId = overviews[index].id;
        dispatch(actions.navigateTo(`/tutor/quiz/${quizId}/${recordId}`));
    };

    const listView = items.length === 0
        ? null
        : <ListView
            items={items}
            onItemClick={onRecordClick} 
            selectedIndex={selectedIndex}
            width={200} />;
    
    return (
        <div className={wrapperClasses.join(' ')}>
            {listView}
            <RecordListControl />
            {isEditing && <div className={classes.Overlay} />}
        </div>
    );
};

export default RecordList;
