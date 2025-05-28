import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

interface NetworkTestResultProps {
  title: string;
  data: any;
}

const NetworkTestResult: React.FC<NetworkTestResultProps> = ({ title, data }) => (
  <View style={styles.resultCard}>
    <Text style={styles.resultTitle}>{title}</Text>
    <Text>{JSON.stringify(data, null, 2)}</Text>
  </View>
);

const styles = StyleSheet.create({
  resultCard: {
    backgroundColor: '#e3f2fd',
    padding: 15,
    borderRadius: 8,
    marginVertical: 10,
    elevation: 2
  },
  resultTitle: {
    fontSize: 18,
    fontWeight: 'bold'
  }
});

export default NetworkTestResult;
