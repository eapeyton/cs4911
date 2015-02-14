module.exports = function(sequelize, DataTypes) {
  var User = sequelize.define("User", {
    id: { 
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    roomId: {
      type: DataTypes.UUID,
      references: 'Rooms',
      referencesKey: 'id',
      allowNull: true
    },
    fbId: {
      type: DataTypes.STRING,
      unique: true,
      allowNull: false
    },
    fbToken: {
      type: DataTypes.STRING,
      allowNull: false
    },
    name: DataTypes.STRING,
    pic: DataTypes.STRING,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        User.belongsTo(models.Room, { foreignKey: 'roomId' });
        User.hasMany(models.Card, { foreignKey: 'userId' });
        User.hasMany(models.Vote, { foreignKey: 'userId' });
      }
    }
  });

  return User;
};