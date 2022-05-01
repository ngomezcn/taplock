# Generated by Django 3.2.4 on 2021-07-18 06:48

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('core', '0005_auto_20210718_0648'),
    ]

    operations = [
        migrations.AlterField(
            model_name='itap',
            name='activation_key',
            field=models.CharField(default='WN3L88KJ', max_length=8),
        ),
        migrations.AlterField(
            model_name='itap',
            name='seed',
            field=models.CharField(default=uuid.UUID('e786c373-90dd-429e-90e9-5d649a90b980'), max_length=36),
        ),
    ]
